package tools.demo.pojo;

/**
 * 支付
 */
public class Trade {

    private Integer id;
    /**
     * 订单id
     */
    private  Long orderId;
    /**
     * 返回结果
     */
    private  String result;
    /**
     * 支付状态 0: 未支付 ，1 支付成功 2支付失败
     */
    private  Integer status;
    /**
     * 生成时间
     */
    private  String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
