package tools.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import tools.demo.pojo.Trade;

/**
 * 支付mapper
 */
@Mapper
@Component
public interface TradeMapper {
    /**
     * 创建支付订单
     * @param orderId
     * @return
     */
    @Insert("INSERT  INTO mx_trade(order_id)VALUES(#{orderId});")
    Integer insertTrade(@Param("orderId")Long orderId);

    /**
     * 修改 支付状态
     * @param orderId 业务订单id
     * @param status 支付状态
     * @return
     */
    @Update("UPDATE mx_trade SET `status`=#{status} WHERE order_id=#{orderId}")
    Integer updateTrade(@Param("orderId") Long orderId,@Param("status") Integer status);

    /**
     * 查看支付订单是否支付过
     * @param orderId
     * @return
     */
    @Select("SELECT `status`  FROM  mx_trade WHERE  order_id=#{orderId}")
    Integer isTrade(@Param("orderId")Long orderId);
}
