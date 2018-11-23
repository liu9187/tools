package tools.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 微信支付业务层
 * 订单流程
 * 用户选择商品提交服务器-》
 * 服务器计算价格、生成业务订单、生成支付订单-》
 * 用户支付-》
 *服务器确认支付结果，更新支付订单状态、更新业务订单状态->
 * 绑定物流
 */
public interface WeChatPayService {
    /**
     * 生成支付订单
     * @param price 价格
     * @param body  主题信息
     * @return  返回信息 发送给客户端
     */
    Map createOrder( BigDecimal price,String body) throws IOException;

    /**
     * 微信服务器调用该请求 ，进行数据异步传回作用
     * @param request
     * @param response
     * @return 一个代表接受成功/失败的信息（失败原因：签名失败，成功则表示确认收到数据，微信不需要再数据息给服务器 ）
     */
    String callBack(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
