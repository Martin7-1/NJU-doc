package cn.edu.nju.software.soa.login.entity;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import cn.edu.nju.software.soa.login.enums.Role;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LoginResponse", namespace = "http://login.soa.software.nju.edu.cn/schema/")
public class LoginResponse {

    private Integer code;
    private String message;
    private Role role;

    public LoginResponse(StatusCode statusCode) {
        this(statusCode, null);
    }

    public LoginResponse(StatusCode statusCode, Role role) {
        this.code = statusCode.getCode();
        this.message = statusCode.getMsg();
        this.role = role;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
