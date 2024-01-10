package cn.edu.nju.software.soa.common.enums;

/**
 * status code and message for response
 */
public enum StatusCode {

    /**
     * 请求成功
     */
    SUCCESS(0, "请求成功"),
    BAD_PARAMETER(4000, "参数异常"),
    /**
     * 邮箱格式错误，通过 '@' 来判断
     */
    INVALID_EMAIL(4001, "邮箱格式错误"),
    /**
     * 密码错误
     */
    INCORRECT_PASSWORD(4002, "密码错误"),
    /**
     * 非有效角色，通过邮箱后缀（即 '@' 之后的字符串）来判断
     */
    INVALID_ROLE(4003, "非有效角色"),
    /**
     * 签到码错误
     */
    INCORRECT_SIGNIN_CODE(4004, "签到码错误"),
    /**
     * 学生不存在
     */
    STUDENT_NOT_FOUND(4005, "学生不存在"),
    /**
     * 没有足够权限
     */
    NO_ENOUGH_AUTH(4006, "没有足够权限");

    private final int code;
    private final String msg;

    StatusCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
