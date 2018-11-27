package tools.demo.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.demo.dao.OrderMapper;
import tools.demo.dao.TradeMapper;
import tools.demo.pojo.Order;
import tools.demo.service.OrderService;

import java.math.BigDecimal;


/**
 * 业务订单实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    private Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TradeMapper tradeMapper;

    /**
     * 查看是否完成过支付任务
     *
     * @param orderId 业务订单
     * @return
     */
    @Override
    public boolean hasProcessed(Long orderId) {
        boolean result = false;
        try {
            //查看 业务订单的状态
            Integer orderStatus = orderMapper.isClose(orderId);
            //查看 支付订单的状态
            Integer tradeStatus = tradeMapper.isTrade(orderId);
            if ((orderStatus == 1) && (tradeStatus != 0))
                result = true;

        } catch (Exception e) {
            log.error("<<<<<<hasProcessed链接数据库出现异常", e);
        }
        return result;
    }

    /**
     * 创建业务订单和支付订单
     *
     * @param order
     * @return
     */
    @Override
    @Transactional
    public boolean insertOrderAndTrade(Order order) {
               boolean result=false;
            Long orderId = order.getOrderId();
            try {
                Integer orderResult = orderMapper.insertOrder(order);
                Integer tradeResult = tradeMapper.insertTrade(orderId);
                if (orderResult>0&&tradeResult>0){
                    log.info("---创建订单表和支付表成功");
                    result=true;
                }
            }catch (Exception e){
                log.error("<<<<<<创建订单表和支付表出现异常",e);
            }
          return  result;

    }
}
