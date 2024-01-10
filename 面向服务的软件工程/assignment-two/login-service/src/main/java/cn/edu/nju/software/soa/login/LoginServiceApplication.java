package cn.edu.nju.software.soa.login;

import cn.edu.nju.software.soa.login.service.impl.LoginServiceImpl;
import cn.edu.nju.software.soa.login.service.LoginService;
import jakarta.xml.ws.Endpoint;

public class LoginServiceApplication {

    private static final String SERVICE_URL = "http://localhost:8080/login";

    public static void main(String[] args) {
        LoginService loginService = new LoginServiceImpl();
        Endpoint.publish(SERVICE_URL, loginService);
    }
}
