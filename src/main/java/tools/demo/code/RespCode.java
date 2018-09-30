package tools.demo.code;

public enum RespCode {
    TOKEN_BAD( "获取token失败", 20000 ), OPERATION_FAILED( "操作失败", 20001 ), PERMISSION_FAILED( "获取权限失败", 20002 ), FILE_FAILED( "文件不存在", 20003 ), PARAMETER_ERROR( "参数错误", 20004 ), FORMAT_ERROR( "文件格式错误", 20005 ), POINTS_EXCHANGE_ERROR( "积分兑换错误", 20006 ), UNKNOWN_ERROR( "服务器异常", 22222 );

    private String describe;
    private int code;

    RespCode(String describe, int code) {
        this.describe = describe;
        this.code = code;
    }

    public String getDescribe() {
        return describe;
    }


    public int getCode() {
        return code;
    }


    public static void main(String[] args) {

    }

}
