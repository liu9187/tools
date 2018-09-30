package tools.demo.code;

/**
 * 响应报文规范
 * @author liuchanglong
 * @date 2018-09-18
 */
public class RespEntity {
    private int code;
    private String msg;
    private Object data;

    public RespEntity(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
