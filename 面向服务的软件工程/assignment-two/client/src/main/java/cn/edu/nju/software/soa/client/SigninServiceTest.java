package cn.edu.nju.software.soa.client;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import cn.edu.nju.software.soa.signin.SigninService;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.URL;

public class SigninServiceTest {

    private static final String SIGNIN_SERVICE_WSDL_URL = "http://localhost:8081/signin?wsdl";
    private static final String SIGNIN_NAMESPACE = "http://signin.soa.software.nju.edu.cn/";

    public static void main(String[] args) {
        SigninService signinService = null;
        try {
            // 创建服务描述的 URL
            URL wsdlURL = new URL(SIGNIN_SERVICE_WSDL_URL);

            // 创建服务的 QName
            QName serviceQName = new QName(SIGNIN_NAMESPACE, "SigninService");

            // 创建服务
            Service service = Service.create(wsdlURL, serviceQName);

            // 获取代理类
            signinService = service.getPort(SigninService.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Test Case 1: Sign in successfully */

        Holder<BigInteger> codeHolder1 = new Holder<>();
        Holder<String> messageHolder1 = new Holder<>();
        Holder<String> statusHolder1 = new Holder<>();
        signinService.signin("201250182@smail.nju.edu.cn", "123456", "123456",
                codeHolder1, messageHolder1, statusHolder1);

        // assert that status code must be StatusCode#success
        System.out.printf("[Test-1] sign in statusCode: %s, message: %s%n", codeHolder1.value, messageHolder1.value);
        assert codeHolder1.value.equals(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));

        /* Test Case 2: Sign in with wrong email format */

        Holder<BigInteger> codeHolder2 = new Holder<>();
        Holder<String> messageHolder2 = new Holder<>();
        Holder<String> statusHolder2 = new Holder<>();
        signinService.signin("201250183", "123456", "123456",
                codeHolder2, messageHolder2, statusHolder2);

        // assert that status code must be StatusCode#success
        System.out.printf("[Test-2] sign in statusCode: %s, message: %s%n", codeHolder2.value, messageHolder2.value);
        assert codeHolder2.value.equals(BigInteger.valueOf(StatusCode.INVALID_EMAIL.getCode()));


        /* Test Case 3: Sign in with wrong password */

        Holder<BigInteger> codeHolder3 = new Holder<>();
        Holder<String> messageHolder3 = new Holder<>();
        Holder<String> statusHolder3 = new Holder<>();
        signinService.signin("201250182@smail.nju.edu.cn", "1234567", "123456",
                codeHolder3, messageHolder3, statusHolder3);

        // assert that status code must be StatusCode#success
        System.out.printf("[Test-3] sign in statusCode: %s, message: %s%n", codeHolder3.value, messageHolder3.value);
        assert codeHolder3.value.equals(BigInteger.valueOf(StatusCode.INCORRECT_PASSWORD.getCode()));

        /* Test Case 4: Sign in with wrong role */

        Holder<BigInteger> codeHolder4 = new Holder<>();
        Holder<String> messageHolder4 = new Holder<>();
        Holder<String> statusHolder4 = new Holder<>();
        signinService.signin("201250182@tsinghua.nju.edu.cn", "123456", "1234567",
                codeHolder4, messageHolder4, statusHolder4);

        // assert that status code must be StatusCode#success
        System.out.printf("[Test-4] sign in statusCode: %s, message: %s%n", codeHolder4.value, messageHolder4.value);
        assert codeHolder4.value.equals(BigInteger.valueOf(StatusCode.INVALID_ROLE.getCode()));


        /* Test Case 5: Sign in with wrong sign in code */

        Holder<BigInteger> codeHolder5 = new Holder<>();
        Holder<String> messageHolder5 = new Holder<>();
        Holder<String> statusHolder5 = new Holder<>();
        signinService.signin("201250182@smail.nju.edu.cn", "123456", "1234567",
                codeHolder5, messageHolder5, statusHolder5);

        // assert that status code must be StatusCode#success
        System.out.printf("[Test-5] sign in statusCode: %s, message: %s%n", codeHolder5.value, messageHolder5.value);
        assert codeHolder5.value.equals(BigInteger.valueOf(StatusCode.INCORRECT_SIGNIN_CODE.getCode()));
    }
}
