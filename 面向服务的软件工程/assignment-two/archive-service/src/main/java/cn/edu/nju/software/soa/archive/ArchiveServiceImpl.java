package cn.edu.nju.software.soa.archive;

import cn.edu.nju.software.soa.common.enums.StatusCode;
import cn.edu.nju.software.soa.login.schema.LoginRequest;
import cn.edu.nju.software.soa.login.schema.LoginResponse;
import cn.edu.nju.software.soa.login.schema.Role;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

import java.util.HashMap;
import java.util.Map;

@WebService(serviceName = "ArchiveService", targetNamespace = "http://archive.soa.software.nju.edu.cn/", name = "ArchiveService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class ArchiveServiceImpl implements ArchiveService {

    private final LoginServiceClient loginServiceClient = new LoginServiceClient();
    /**
     * mock data of student infos
     */
    private static final Map<String, StudentInfo> MOCK_DATA = new HashMap<>();

    static {
        MOCK_DATA.put("1", new StudentInfo("undergraduate-1", "1", "201250182@smail.nju.edu.cn", Gender.MALE));
        MOCK_DATA.put("2", new StudentInfo("graduate-1", "2", "522022320130@smail.nju.edu.cn", Gender.FEMALE));
        MOCK_DATA.put("3", new StudentInfo("teacher-1", "3", "test@nju.edu.cn", Gender.MALE));
    }

    @Override
    public UpdateResponse update(UpdateRequest updateRequest) {
        UpdateResponse updateResponse;

        LoginResponse loginResponse = handleLogin(updateRequest.getLoginInfo());
        if (loginResponse.getCode() != StatusCode.SUCCESS.getCode()) {
            return new UpdateResponse(loginResponse.getCode(), loginResponse.getMessage());
        } else if (loginResponse.getRole() != Role.TEACHER) {
            // 只有老师可以更新
            return new UpdateResponse(StatusCode.NO_ENOUGH_AUTH);
        }

        if (!MOCK_DATA.containsKey(updateRequest.getId())) {
            // 没有该学生数据
            updateResponse = new UpdateResponse(StatusCode.STUDENT_NOT_FOUND);
            return updateResponse;
        }

        // update
        StudentInfo existInfo = MOCK_DATA.get(updateRequest.getId());
        if (updateRequest.getName() != null) {
            existInfo.setName(updateRequest.getName());
        }
        if (updateRequest.getEmail() != null) {
            boolean isVerifiedEmail = verifyEmail(updateRequest.getEmail());
            if (!isVerifiedEmail) {
                return new UpdateResponse(StatusCode.INVALID_EMAIL);
            }
            existInfo.setEmail(updateRequest.getEmail());
        }
        if (updateRequest.getGender() != null) {
            existInfo.setGender(updateRequest.getGender());
        }
        updateResponse = new UpdateResponse(StatusCode.SUCCESS, existInfo);

        return updateResponse;
    }

    @Override
    public AddResponse add(AddRequest addRequest) {
        LoginResponse loginResponse = handleLogin(addRequest.getLoginInfo());
        if (loginResponse.getCode() != StatusCode.SUCCESS.getCode()) {
            return new AddResponse(loginResponse.getCode(), loginResponse.getMessage());
        } else if (loginResponse.getRole() != Role.TEACHER) {
            // 只有老师可以更新
            return new AddResponse(StatusCode.NO_ENOUGH_AUTH);
        }

        boolean isVerifiedEmail = verifyEmail(addRequest.getEmail());
        if (!isVerifiedEmail) {
            return new AddResponse(StatusCode.INVALID_EMAIL);
        }

        if (addRequest.getEmail() == null || addRequest.getId() == null
                || addRequest.getName() == null || addRequest.getGender() == null) {
            return new AddResponse(StatusCode.BAD_PARAMETER);
        }

        // add info
        MOCK_DATA.put(addRequest.getId(), new StudentInfo(
                addRequest.getName(),
                addRequest.getId(),
                addRequest.getEmail(),
                addRequest.getGender()
        ));

        return new AddResponse(StatusCode.SUCCESS, true);
    }

    @Override
    public QueryResponse query(QueryRequest queryRequest) {
        LoginResponse loginResponse = handleLogin(queryRequest.getEmail(), queryRequest.getPassword());
        if (loginResponse.getCode() != StatusCode.SUCCESS.getCode()) {
            return new QueryResponse(loginResponse.getCode(), loginResponse.getMessage());
        }

        StudentInfo studentInfo = null;
        for (Map.Entry<String, StudentInfo> entry : MOCK_DATA.entrySet()) {
            if (queryRequest.getEmail().equals(entry.getValue().getEmail())) {
                studentInfo = entry.getValue();
                break;
            }
        }

        if (studentInfo == null) {
            return new QueryResponse(StatusCode.STUDENT_NOT_FOUND);
        }

        return new QueryResponse(StatusCode.SUCCESS, studentInfo);
    }

    @Override
    public DeleteResponse delete(DeleteRequest deleteRequest) {
        LoginResponse loginResponse = handleLogin(deleteRequest.getLoginInfo());
        if (loginResponse.getCode() != StatusCode.SUCCESS.getCode()) {
            return new DeleteResponse(loginResponse.getCode(), loginResponse.getMessage());
        } else if (loginResponse.getRole() != Role.TEACHER) {
            // 只有老师可以更新
            return new DeleteResponse(StatusCode.NO_ENOUGH_AUTH);
        }

        // delete info
        if (!MOCK_DATA.containsKey(deleteRequest.getId())) {
            // 没有该学生数据
            return new DeleteResponse(StatusCode.STUDENT_NOT_FOUND);
        }

        MOCK_DATA.remove(deleteRequest.getId());

        return new DeleteResponse(StatusCode.SUCCESS, true);
    }

    private LoginResponse handleLogin(LoginInfo loginInfo) {
        return handleLogin(loginInfo.getEmail(), loginInfo.getPassword());
    }

    private LoginResponse handleLogin(String email, String password) {
        // 统一身份验证校验
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        return loginServiceClient.login(loginRequest);
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
}
