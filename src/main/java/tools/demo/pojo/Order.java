package tools.demo.pojo;

import java.math.BigDecimal;

/**
 * 订单
 */
public class Order {
    /**
     * 订单号
     */
   private  Long orderId;
    /**
     * 价格
     */
   private BigDecimal price;
    /**
     * 标题
     */
   private  String body;
    /**
     * 订单状态
     */
   private  Integer status;
    /**
     * 生成时间
     */
    private  String createTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
