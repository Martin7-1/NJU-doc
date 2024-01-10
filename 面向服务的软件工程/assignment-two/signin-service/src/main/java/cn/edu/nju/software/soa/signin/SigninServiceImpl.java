package cn.edu.nju.software.soa.signin;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import cn.edu.nju.software.soa.login.schema.LoginRequest;
import cn.edu.nju.software.soa.login.schema.LoginResponse;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;

@WebService(serviceName = "SigninService", targetNamespace = "http://signin.soa.software.nju.edu.cn/", name = "SigninService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class SigninServiceImpl implements SigninService {

    private final LoginServiceClient loginServiceClient = new LoginServiceClient();
    /**
     * mock data of sign in
     */
    private static final String MOCK_SIGNIN_CODE = "123456";
    private static final LocalDateTime MOCK_SIGNIN_TIME = LocalDateTime.now(ZoneId.of("UTC+8"));

    @Override
    public SigninResponse signin(SigninRequest parameters) {
        SigninResponse response;

        response = handleLogin(parameters.getEmail(), parameters.getPassword());
        if (response != null) {
            return response;
        }

        // check sign in code correct
        response = new SigninResponse();
        if (!MOCK_SIGNIN_CODE.equals(parameters.getSigninCode())) {
            response.setCode(BigInteger.valueOf(StatusCode.INCORRECT_SIGNIN_CODE.getCode()));
            response.setMessage(StatusCode.INCORRECT_SIGNIN_CODE.getMsg());
            return response;
        }

        // sign in successfully
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+8"));
        response.setCode(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));
        response.setMessage(StatusCode.SUCCESS.getMsg());
        response.setStatus(now.isBefore(MOCK_SIGNIN_TIME) ? Status.ON_TIME : Status.LATE);

        return response;
    }

    private SigninResponse handleLogin(String email, String password) {
        SigninResponse signinResponse = new SigninResponse();
        // 统一身份验证校验
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        LoginResponse response = loginServiceClient.login(loginRequest);

        int code = response.getCode();
        // handle error
        if (code == StatusCode.SUCCESS.getCode()) {
            signinResponse = null;
        } else if (code == StatusCode.INVALID_EMAIL.getCode()) {
            signinResponse.setCode(BigInteger.valueOf(StatusCode.INVALID_EMAIL.getCode()));
            signinResponse.setMessage(StatusCode.INVALID_EMAIL.getMsg());
        } else if (code == StatusCode.INCORRECT_PASSWORD.getCode()) {
            signinResponse.setCode(BigInteger.valueOf(StatusCode.INCORRECT_PASSWORD.getCode()));
            signinResponse.setMessage(StatusCode.INCORRECT_PASSWORD.getMsg());
        } else if (code == StatusCode.INVALID_ROLE.getCode()) {
            signinResponse.setCode(BigInteger.valueOf(StatusCode.INVALID_ROLE.getCode()));
            signinResponse.setMessage(StatusCode.INVALID_ROLE.getMsg());
        }

        return signinResponse;
    }
}
