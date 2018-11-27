package tools.demo.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tools.demo.pojo.Order;
import tools.demo.service.OrderService;
import tools.demo.service.WeChatPayService;
import tools.demo.utils.JnbEsbUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * 业务控制器
 */
@RestController
@RequestMapping("/api/v2/demo")
public class OrderController {
    private Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private WeChatPayService wxPayService;

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/createOrder", method = {RequestMethod.POST})
    public String createOrder(BigDecimal price, String body) {
        //生成订单号
        String orderIdStr = JnbEsbUtil.CreateMsgId();
        Long orderId = Long.valueOf(orderIdStr);
        Order order = new Order();
        order.setBody(body);
        order.setPrice(price);
        order.setOrderId(orderId);
        Map<String, Object> map = new TreeMap<>();
        JSONObject object = new JSONObject();
        try {
            boolean result = orderService.insertOrderAndTrade(order);
            if (result) {
                log.info("-----订单创建完成，请求微信服务器.........");
                map = wxPayService.createOrder(price, body);
            }
        } catch (Exception e) {
            object.put("message","生成订单出现异常");
        }

        object.put("map", map);
        object.put("message","创建订单成功");
        return object.toJSONString();
    }
}
