package tools.demo.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import tools.demo.pojo.Order;

/**
 * mapper
 */
@Mapper
@Component
public interface OrderMapper {
    /**
     * 创建业务订单
     * @param order
     * @return
     */
    @Insert("INSERT INTO mx_order(order_id,price,body) VALUES(#{orderId},#{price},#{body})")
    Integer insertOrder(Order order);

    /**
     * 业务订单是否关闭
     * @return
     */
    @Select("SELECT `status` FROM mx_order where order_id=#{orderId};")
    Integer isClose(@Param("orderId") Long orderId);

    /**
     * 已处理，关闭订单
     * @param orderId
     * @return
     */
    @Update("UPDATE mx_order SET `status`=1 WHERE order_id=#{orderId}")
    Integer CloseOrder(@Param("orderId") Long orderId);

}
