package cn.edu.nju.software.soa.signin;

import cn.edu.nju.software.soa.login.LoginService;
import cn.edu.nju.software.soa.login.schema.LoginRequest;
import cn.edu.nju.software.soa.login.schema.LoginResponse;
import jakarta.xml.ws.Endpoint;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.net.URL;

public class LoginServiceClient {

    private static final String LOGIN_SERVICE_WSDL_URL = "http://localhost:8080/login?wsdl";
    private static final String LOGIN_NAMESPACE = "http://login.soa.software.nju.edu.cn/";

    public LoginResponse login(LoginRequest request) {
        try {
            // 创建服务描述的 URL
            URL wsdlURL = new URL(LOGIN_SERVICE_WSDL_URL);

            // 创建服务的 QName
            QName serviceQName = new QName(LOGIN_NAMESPACE, "LoginService");

            // 创建服务
            Service service = Service.create(wsdlURL, serviceQName);

            // 获取代理类
            LoginService port = service.getPort(LoginService.class);

            // 调用 Web Service 的操作
            return port.login(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
