package cn.edu.nju.software.soa.login.service;

import cn.edu.nju.software.soa.login.entity.LoginRequest;
import cn.edu.nju.software.soa.login.entity.LoginResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService(name = "LoginService", serviceName = "LoginService", targetNamespace = "http://login.soa.software.nju.edu.cn/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface LoginService {

    /**
     * 邮箱登陆
     *
     * <p>验证邮箱身份 并 返回对应的身份信息</p>
     *
     * @param loginRequest 登陆请求
     * @return 登陆响应
     */
    @WebMethod
    LoginResponse login(LoginRequest loginRequest);
}
