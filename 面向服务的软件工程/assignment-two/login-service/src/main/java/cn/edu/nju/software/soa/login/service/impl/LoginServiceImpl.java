package cn.edu.nju.software.soa.login.service.impl;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import cn.edu.nju.software.soa.login.entity.LoginRequest;
import cn.edu.nju.software.soa.login.entity.LoginResponse;
import cn.edu.nju.software.soa.login.enums.Role;
import cn.edu.nju.software.soa.login.service.LoginService;
import jakarta.jws.WebService;

import java.util.Map;

/**
 * Login service implementation
 */
@WebService(name = "LoginService", serviceName = "LoginService", targetNamespace = "http://login.soa.software.nju.edu.cn/")
public class LoginServiceImpl implements LoginService {

    private static final int UNDERGRADUATE_PREFIX_LENGTH = 9;
    private static final String TEACHER_POSTFIX = "nju.edu.cn";
    private static final String STUDENT_POSTFIX = "smail.nju.edu.cn";

    /**
     * mock data to test
     */
    private static final Map<String, String> MOCK_DATA = Map.of(
            // undergraduate email
            "201250182@smail.nju.edu.cn", "123456",
            // graduate email
            "522022320130@smail.nju.edu.cn", "123456",
            // teacher email
            "test@nju.edu.cn", "123456"
    );

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // check whether parameter is null
        if (loginRequest == null) {
            return new LoginResponse(StatusCode.BAD_PARAMETER);
        }
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // verify email
        boolean isVerifiedEmail = verifyEmail(email);
        if (!isVerifiedEmail) {
            return new LoginResponse(StatusCode.INVALID_EMAIL);
        }

        // verify role of email
        Role role = getRoleFromEmail(email);
        if (role == null) {
            return new LoginResponse(StatusCode.INVALID_ROLE);
        }

        // verify password
        boolean isCorrectPassword = verifyPassword(email, password);
        if (!isCorrectPassword) {
            return new LoginResponse(StatusCode.INCORRECT_PASSWORD);
        }

        return new LoginResponse(StatusCode.SUCCESS, role);
    }

    private boolean verifyEmail(String email) {
        return email != null &&
                // email contains @
                email.contains("@") &&
                // @ is not the first element of email
                email.indexOf("@") != 0 &&
                // @ is not the last element of email
                email.indexOf("@") != email.length() - 1;
    }

    private boolean verifyPassword(String email, String password) {
        String correctPassword = MOCK_DATA.get(email);
        return correctPassword != null && correctPassword.equals(password);
    }

    private Role getRoleFromEmail(String email) {
        String prefix = email.split("@")[0];
        String postfix = email.split("@")[1];

        if (TEACHER_POSTFIX.equals(postfix)) {
            return Role.TEACHER;
        } else if (STUDENT_POSTFIX.equals(postfix)) {
            return prefix.length() == UNDERGRADUATE_PREFIX_LENGTH ?
                    Role.UNDERGRADUATE : Role.GRADUATE;
        }

        return null;
    }
}
