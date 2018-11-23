package tools.demo.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 微信支付工具类
 */
@Service
public class WxUtils {
    /**
     * post请求
     *
     * @param url
     * @param parameters
     * @return
     * @throws IOException
     */
    public String executeHttpPost(String url, SortedMap<String, Object> parameters) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-type", "application/xml");
        request.setHeader("Accept", "application/xml");
        request.setEntity(new StringEntity(transferMapToXml(parameters), "UTF-8"));
        HttpResponse httpRespone = httpClient.execute(request);
        return readResponse(httpRespone);
    }

    /**
     * map 转换成xml
     *
     * @param map
     * @return
     */
    public String transferMapToXml(SortedMap<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : map.keySet()) {
            sb.append("<").append(key).append(">")
                    .append(map.get(key))
                    .append("<").append(key).append(">");
        }

        return sb.append("</xml>").toString();
    }

    /**
     * xml 转化为map
     *
     * @param strxml
     * @return
     */
    public Map transferXmlToMap(String strxml) throws IOException {
        strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null == strxml || "".equals(strxml)) {
            return null;
        }
        Map m = new HashMap();
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        try {
            doc = builder.build(in);
        } catch (JDOMException e) {
            //转化为io异常抛出
            throw new IOException(e.getMessage());
        }
        //解析dom
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List childrens = e.getChildren();
            if (childrens.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(childrens);
            }
            m.put(k, v);
        }
        //关闭输入流
        in.close();

        return m;
    }

    /**
     * transferXmlToMap 辅助类
     * 递归提取子节点
     *
     * @param childrens
     * @return
     */
    private String getChildrenText(List<Element> childrens) {
        StringBuffer sb = new StringBuffer();
        if (!childrens.isEmpty()) {
            Iterator<Element> it = childrens.iterator();
            while (it.hasNext()) {
                Element e = it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                 List<Element> list=e.getChildren();
                 sb.append("<"+name+">");
                 if (!list.isEmpty()){
                     sb.append(getChildrenText(list));
                 }
                 sb.append(value);
                 sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }

    /**
     * 读取request的内容
     * @param request
     * @return 请求内容字符串
     * @throws IOException
     */
    public String readRequest(HttpServletRequest request) throws IOException{
          InputStream inputStream= request.getInputStream();
           BufferedReader in=new BufferedReader(new InputStreamReader(inputStream));
           StringBuffer sb=new StringBuffer();
           String str;
           while (null !=(str=in.readLine())){
                  sb.append(str);
           }
           in.close();
           inputStream.close();
           return sb.toString();
    }

    /**
     * 读取 response 的内容
     *
     * @param response
     * @return 字符串
     * @throws IOException
     */
    public String readResponse(HttpResponse response) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String result = new String();
        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }

    /**
     * 第一次签名
     *
     * @param parameters 服务器生成，下单时必须的字段排序签名
     * @param key
     * @return
     */
    public String createSign(SortedMap<String, Object> parameters, String key) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }

        }
        sb.append("key=" + key);
        return encodeMD5(sb.toString());
    }

    /**
     * 第二次签名
     *
     * @param result 微信返回给服务器的 数据（xml的String）再次签名后传给客户端
     * @param key
     * @return
     */
    public Map createSign2(String result, String key) throws IOException {
          SortedMap<String,Object> map=new TreeMap<>(transferXmlToMap(result));
          Map app=new HashMap();
             app.put("appid",map.get("appid"));
             app.put("partnerid",map.get("mch_id"));
             app.put("prepayid",map.get("prepay_id"));
             //固定字段 保留不可以修改
             app.put("package", "Sign=WXPay");
             app.put("noncestr",map.get("nonce_str"));
             //时间秒
             app.put("timestamp",new Date().getTime() / 1000);
             app.put("sign", createSign(new TreeMap<>(app), key));
        return app;
    }

    /**
     * 验证签名是否正确
     * @param parameters
     * @param key
     * @return
     */
    public boolean checkSign(SortedMap<String, Object> parameters, String key){
        String signWx = parameters.get("sign").toString();
          if (null==signWx){
              return false;
          }
          //去掉 sign 的键值 重新签名
           parameters.remove("sign");
        String signMe = createSign(parameters, key);

        return signWx.equals(signMe);
    }

    /**
     * MD5 签名
     *
     * @param str
     * @return 签名后的字符串信息
     */
    public String encodeMD5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = str.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 辅助md5方法实现
     *
     * @param byteArray
     * @return
     */
    private String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        //返回字符串
        return new String(resultCharArray);
    }

    /**
     * 生成32位随机字符
     * @return
     */
    public String gen32RandomString(){
        char[] dict = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuffer sb=new StringBuffer();
        Random random=new Random();
        for (int i=0;i<32;i++){
            sb.append(String.valueOf(dict[(int) (Math.random()*62)]));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String s= new WxUtils().gen32RandomString();
        System.out.println(s);
    }

}
