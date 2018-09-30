package tools.demo.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口请求
 */
public class httpUtils {
    //请求路径
    private URL httpurl;
    //返回结果
    private StringBuilder result;
    //url连接
    private HttpURLConnection httpConn = null;
    private InputStream inputStream = null;
    private OutputStream outputStream=null;
    private BufferedReader bufferedReader = null;

    /**
     * GET、PUT 请求
     *
     * @param url     请求路径
     * @param headers 请求头
     * @param method 请求方法
     * @return
     * @throws IOException
     */
    public String request1(String url, Map<String, String> headers,String method) throws IOException {
        result = new StringBuilder();
        try {
            //请求地址
            httpurl = new URL( url );
            //打开连接
            httpConn = (HttpURLConnection) httpurl.openConnection();
            //设置请求方式
            httpConn.setRequestMethod( method );
            //设置header
            for (String key : headers.keySet()) {
                httpConn.setRequestProperty( key, headers.get( key ) );
            }
            //打开输入流
            httpConn.setDoInput( true );
            //数据写入
            httpConn.getInputStream();
            if (httpConn.getResponseCode() != 200 && httpConn.getResponseCode() != 201 && httpConn.getResponseCode() != 202 && httpConn.getResponseCode() != 204) {
                return new String( "接口调用失败" );
            }
            bufferedReader = new BufferedReader( new InputStreamReader( httpConn.getInputStream(), "utf-8" ) );
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append( line );
            }

        } finally {
            if (null != bufferedReader) {
                bufferedReader.close();
            }
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != httpConn) {
                httpConn.disconnect();
            }

        }
        return result.toString();
    }

    /**
     * post 请求
     * @param url 路径
     * @param headers 请求头
     * @return 响应结果
     * @throws IOException
     */
    public String request2(String url, Map<String, String> headers, String params) throws IOException {
        result = new StringBuilder();
        try {
            //请求地址
            httpurl = new URL( url );
            //打开url连接
            httpConn= (HttpURLConnection) httpurl.openConnection();
            //请求方法
            httpConn.setRequestMethod( "POST" );
            //设置请求头
            for (String key:headers.keySet()){
                httpConn.setRequestProperty( key,headers.get( key ) );
            }
            //打开输入流
            httpConn.setDoInput( true );
            //打开输出流
            httpConn.setDoOutput( true );
            //判断响应状态
             outputStream=httpConn.getOutputStream();
             outputStream.write( params.getBytes("UTF-8"));
             outputStream.flush();
           if (httpConn.getResponseCode() != 200 && httpConn.getResponseCode() != 201 && httpConn.getResponseCode() != 202 && httpConn.getResponseCode() != 204){
                  System.out.println( httpConn.getResponseCode() );
                inputStream=httpConn.getErrorStream();
               return "请求失败";
           }else{
               inputStream=httpConn.getInputStream();
           }
            //请求成功写入数据
              bufferedReader= new BufferedReader( new InputStreamReader( inputStream ,"UTF-8") );
           String line;
              while ((line=bufferedReader.readLine() )!=null){
                    result.append( line );
              }
        }  finally {
               if (null != bufferedReader){
                   bufferedReader.close();
               }
               if (null !=inputStream){
                    inputStream.close();
               }
               if (null !=outputStream){
                   outputStream.close();
               }
               if (null != httpConn){
                   httpConn.disconnect();
               }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        //  String json="{\"cipherText\":\"fG3Ts59IhpJYy5HxRzpTmw==\"}";
       // System.out.println(json);
        String params="cipherText=fG3Ts59IhpJYy5HxRzpTmw==";
        httpUtils httpUtils = new httpUtils();
        String url = "http://192.168.100.218:9010/api/v2/encrypt";
        Map<String, String> headers = new HashMap<>();
        //  httpConn.setRequestProperty("Content-Type", "application/json");
//        headers.put( "Authorization", "Bearer PIpAmzACbXy3UipssW9Dc1UtqmoQmnZTagKRJuqsZbvaHV4x " );
      //  String method="PUT";
        String s = null;
        try {
            s = httpUtils.request2( url, headers,params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println( s );
    }
}
