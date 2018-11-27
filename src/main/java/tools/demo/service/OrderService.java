package tools.demo.service;

import tools.demo.pojo.Order;

/**
 * 订单业务
 */
public interface OrderService {
    /**
     * 判断订单是不是已经关闭
     * @param orderId
     * @return
     */
    boolean hasProcessed( Long orderId);

    /**
     * 创建业务订单和支付订单
     * @param order
     * @return
     */
    boolean  insertOrderAndTrade(Order order);


}
