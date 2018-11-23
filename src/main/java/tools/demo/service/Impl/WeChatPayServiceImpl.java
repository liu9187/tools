package tools.demo.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.demo.service.OrderService;
import tools.demo.service.WeChatPayService;
import tools.demo.utils.JnbEsbUtil;
import tools.demo.utils.WxUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付实现类
 */
@Service
public class WeChatPayServiceImpl implements WeChatPayService {
    private Logger log = LoggerFactory.getLogger(WeChatPayServiceImpl.class);
    @Value("${wxpay.appid}")
    private String appid;

    @Value("${wxpay.mchid}")
    private String mchid;

    @Value("${wxpay.key}")
    private String key;

    @Value("${wxpay.notifuy.url}")
    private String notifuyrl;

    @Value("${ipAddress}")
    private String ipAddress;

    @Autowired
    WxUtils wxUtils;

    // 订单处理类，负责处理一个订单成功／失败后的业务逻辑
    @Autowired
    private OrderService orderService;


    // 下单 API 地址
    private String placeUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    @Override
    public Map createOrder(BigDecimal price, String body) throws IOException {
        //生成订单号
        String orderId = JnbEsbUtil.CreateMsgId();
        //生成业务订单和支付订单

        //调用微信流程
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", appid);
        parameters.put("mch_id", mchid);
        //默认
        parameters.put("device_info", "WEB");
        parameters.put("body", body);
        //生成32位随机码
        parameters.put("nonce_str", wxUtils.gen32RandomString());
        parameters.put("notify_url", notifuyrl);
        parameters.put("out_trade_no", orderId);
        //把价格的单位设置为元
        parameters.put("total_fee", price.multiply(BigDecimal.valueOf(100)).intValue());
        // parameters.put("total_fee", 1); // 测试时，将支付金额设置为 1 分钱
        parameters.put("spbill_create_ip", ipAddress);
        parameters.put("trade_type", "APP");
        // sign 必须在最后
        parameters.put("sign", wxUtils.createSign(parameters, key));
        //发送给微信
        String result = wxUtils.executeHttpPost(placeUrl, parameters);
        return wxUtils.createSign2(result, key);
    }

    @Override
    @Transactional
    public String callBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //预先设定返回的状态为 xml
        response.setHeader("Content-type", "application/xml");
        //读取参数解析xml 为map
        Map<String, String> map = wxUtils.transferXmlToMap(wxUtils.readRequest(request));
        //转换有序的map 判断签名是否正确
        boolean isSignSuccess = wxUtils.checkSign(new TreeMap<>(map), key);
        if (isSignSuccess) {
            //签名成功说明是微信服务器发送的数据
            //获取订单号
            String orderIdStr = map.get("out_trade_no");
            Long orderId = null;
            if (null != orderIdStr) {
                orderId = Long.valueOf(orderIdStr);
            }
            // 判断该订单是否已经被接收处理过
            if (orderService.hasProcessed(orderId)) {
                //如果已经处理
                log.info("---订单：" + orderIdStr + "订单已经处理");
                //接业务
                return success();
            }
            //如果没有处理 1.修改业务订单状态为支付状态 2.保存map到数据库 3.修改数据库的状态
            //保存map数据

            //判断返回值
            if (map.get("return_code").equals("SUCCESS")) {
                //初始化一个成功或者失败的标识
                Integer status = 0;
                if (map.get("result_code").equals("SUCCESS")) {
                    //支付成功
                    status = 1;
                } else {
                    //支付失败
                    // orderService.failOrder(orderId);
                    status = 2;
                }
                log.info("--签名成功，支付状态status=");
                //做业务回掉处理 异步处理
            }

            return success();
        } else {
            // 签名校验失败（可能不是微信服务器发出的数据）
            return fail();

        }
    }

    String fail() {
        return "<xml>\n" +
                "  <return_code><![CDATA[FAIL]]></return_code>\n" +
                "  <return_msg><![CDATA[]]></return_msg>\n" +
                "</xml>";
    }

    String success() {
        return "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
    }

}
