package cn.edu.nju.software.soa.signin;

import jakarta.xml.ws.Endpoint;

public class SigninServiceApplication {

    private static final String SERVICE_URL = "http://localhost:8081/signin";

    public static void main(String[] args) {
        SigninService signinService = new SigninServiceImpl();
        Endpoint.publish(SERVICE_URL, signinService);
    }
}
