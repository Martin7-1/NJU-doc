package cn.edu.nju.software.soa.client;

import cn.edu.nju.software.soa.archive.ArchiveService;
import cn.edu.nju.software.soa.archive.schema.Gender;
import cn.edu.nju.software.soa.archive.schema.LoginInfo;
import cn.edu.nju.software.soa.archive.schema.StudentInfo;
import cn.edu.nju.software.soa.common.enums.StatusCode;
import jakarta.xml.ws.Holder;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.URL;

public class ArchiveServiceTest {

    private static final String ARCHIVE_SERVICE_WSDL_URL = "http://localhost:8082/archive?wsdl";
    private static final String ARCHIVE_NAMESPACE = "http://archive.soa.software.nju.edu.cn/";
    private static ArchiveService archiveService;

    public static void main(String[] args) {
        try {
            // 创建服务描述的 URL
            URL wsdlURL = new URL(ARCHIVE_SERVICE_WSDL_URL);

            // 创建服务的 QName
            QName serviceQName = new QName(ARCHIVE_NAMESPACE, "ArchiveService");

            // 创建服务
            Service service = Service.create(wsdlURL, serviceQName);

            // 获取代理类
            archiveService = service.getPort(ArchiveService.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        testAdd();
        testQuery();
        testUpdate();
        testDelete();
    }

    private static void testAdd() {
        System.out.println("========== Test Add Method ==========");

        /* Test Case 1: add StudentInfo successfully */

        Holder<BigInteger> codeHolder1 = new Holder<>();
        Holder<String> messageHolder1 = new Holder<>();
        Holder<Boolean> dataHolder1 = new Holder<>();
        LoginInfo loginInfo1 = new LoginInfo();
        loginInfo1.setEmail("test@nju.edu.cn");
        loginInfo1.setPassword("123456");
        archiveService.add(loginInfo1, "undergraduate-2", "4", "201250183@smail.nju.edu.cn",
                Gender.MALE.value(), "123456", codeHolder1, messageHolder1, dataHolder1);

        System.out.printf("[Add-Test-1] add statusCode: %s, message: %s, data: %s%n", codeHolder1.value, messageHolder1.value, dataHolder1.value);
        assert codeHolder1.value.equals(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));

        /* Test Case 2: operator's role must be teacher */

        Holder<BigInteger> codeHolder2 = new Holder<>();
        Holder<String> messageHolder2 = new Holder<>();
        Holder<Boolean> dataHolder2 = new Holder<>();
        LoginInfo loginInfo2 = new LoginInfo();
        loginInfo2.setEmail("201250182@smail.nju.edu.cn");
        loginInfo2.setPassword("123456");
        archiveService.add(loginInfo2, "undergraduate-2", "4", "201250183@smail.nju.edu.cn",
                Gender.MALE.value(), "123456", codeHolder2, messageHolder2, dataHolder2);

        System.out.printf("[Add-Test-2] add statusCode: %s, message: %s, data: %s%n", codeHolder2.value, messageHolder2.value, dataHolder2.value);
        assert codeHolder2.value.equals(BigInteger.valueOf(StatusCode.NO_ENOUGH_AUTH.getCode()));

        /* Test Case 3: email format correct */

        Holder<BigInteger> codeHolder3 = new Holder<>();
        Holder<String> messageHolder3 = new Holder<>();
        Holder<Boolean> dataHolder3 = new Holder<>();
        LoginInfo loginInfo3 = new LoginInfo();
        loginInfo3.setEmail("test@nju.edu.cn");
        loginInfo3.setPassword("123456");
        archiveService.add(loginInfo3, "undergraduate-2", "4", "201250183",
                Gender.MALE.value(), "123456", codeHolder3, messageHolder3, dataHolder3);

        System.out.printf("[Add-Test-3] add statusCode: %s, message: %s, data: %s%n", codeHolder3.value, messageHolder3.value, dataHolder3.value);
        assert codeHolder3.value.equals(BigInteger.valueOf(StatusCode.NO_ENOUGH_AUTH.getCode()));

        /* Test Case 4: attribute should not be null */

        Holder<BigInteger> codeHolder4 = new Holder<>();
        Holder<String> messageHolder4 = new Holder<>();
        Holder<Boolean> dataHolder4 = new Holder<>();
        LoginInfo loginInfo4 = new LoginInfo();
        loginInfo4.setEmail("test@nju.edu.cn");
        loginInfo4.setPassword("123456");
        archiveService.add(loginInfo4, null, "4", "201250183@smail.nju.edu.cn",
                Gender.MALE.value(), "123456", codeHolder4, messageHolder4, dataHolder4);

        System.out.printf("[Add-Test-4] add statusCode: %s, message: %s, data: %s%n", codeHolder4.value, messageHolder4.value, dataHolder4.value);
        assert codeHolder4.value.equals(BigInteger.valueOf(StatusCode.BAD_PARAMETER.getCode()));

        System.out.println();
    }

    private static void testQuery() {
        System.out.println("========== Test Query Method ==========");

        /* Test Case 1: query successfully */

        Holder<BigInteger> codeHolder1 = new Holder<>();
        Holder<String> messageHolder1 = new Holder<>();
        Holder<StudentInfo> dataHolder1 = new Holder<>();
        archiveService.query("201250182@smail.nju.edu.cn", "123456", codeHolder1, messageHolder1, dataHolder1);

        System.out.printf("[Query-Test-1] query statusCode: %s, message: %s, data: %s%n", codeHolder1.value, messageHolder1.value, dataHolder1.value.toString());
        assert codeHolder1.value.equals(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));

        System.out.println();
    }

    private static void testUpdate() {
        System.out.println("========== Test Update Method ==========");

        /* Test Case 1: update successfully */

        Holder<BigInteger> codeHolder1 = new Holder<>();
        Holder<String> messageHolder1 = new Holder<>();
        Holder<StudentInfo> dataHolder1 = new Holder<>();
        LoginInfo loginInfo1 = new LoginInfo();
        loginInfo1.setEmail("test@nju.edu.cn");
        loginInfo1.setPassword("123456");
        archiveService.update(loginInfo1, "undergraduate-2", "1", null, null, null,
                codeHolder1, messageHolder1, dataHolder1);

        System.out.printf("[Update-Test-1] update statusCode: %s, message: %s, data: %s%n", codeHolder1.value, messageHolder1.value, dataHolder1.value.toString());
        assert codeHolder1.value.equals(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));

        /* Test Case 2: operator's role must be teacher */

        Holder<BigInteger> codeHolder2 = new Holder<>();
        Holder<String> messageHolder2 = new Holder<>();
        Holder<StudentInfo> dataHolder2 = new Holder<>();
        LoginInfo loginInfo2 = new LoginInfo();
        loginInfo2.setEmail("201250182@smail.nju.edu.cn");
        loginInfo2.setPassword("123456");
        archiveService.update(loginInfo2, "undergraduate-2", "1", null, null, null,
                codeHolder2, messageHolder2, dataHolder2);

        System.out.printf("[Update-Test-2] update statusCode: %s, message: %s%n", codeHolder2.value, messageHolder2.value);
        assert codeHolder2.value.equals(BigInteger.valueOf(StatusCode.NO_ENOUGH_AUTH.getCode()));

        /* Test Case 3: update attribute format error */

        Holder<BigInteger> codeHolder3 = new Holder<>();
        Holder<String> messageHolder3 = new Holder<>();
        Holder<StudentInfo> dataHolder3 = new Holder<>();
        LoginInfo loginInfo3 = new LoginInfo();
        loginInfo3.setEmail("test@nju.edu.cn");
        loginInfo3.setPassword("123456");
        archiveService.update(loginInfo3, null, "1", "201250183", null, null,
                codeHolder3, messageHolder3, dataHolder3);

        System.out.printf("[Update-Test-3] update statusCode: %s, message: %s%n", codeHolder3.value, messageHolder3.value);
        assert codeHolder3.value.equals(BigInteger.valueOf(StatusCode.INVALID_EMAIL.getCode()));

        /* Test Case 4: update non-exist student info */

        Holder<BigInteger> codeHolder4 = new Holder<>();
        Holder<String> messageHolder4 = new Holder<>();
        Holder<StudentInfo> dataHolder4 = new Holder<>();
        LoginInfo loginInfo4 = new LoginInfo();
        loginInfo4.setEmail("test@nju.edu.cn");
        loginInfo4.setPassword("123456");
        archiveService.update(loginInfo4, "undergraduate-2", "100", null, null, null,
                codeHolder4, messageHolder4, dataHolder4);

        System.out.printf("[Update-Test-2] update statusCode: %s, message: %s%n", codeHolder4.value, messageHolder4.value);
        assert codeHolder4.value.equals(BigInteger.valueOf(StatusCode.STUDENT_NOT_FOUND.getCode()));

        System.out.println();
    }

    private static void testDelete() {
        System.out.println("========== Test Delete Method ==========");

        /* Test Case 1: delete successfully */

        Holder<BigInteger> codeHolder1 = new Holder<>();
        Holder<String> messageHolder1 = new Holder<>();
        Holder<Boolean> dataHolder1 = new Holder<>();
        LoginInfo loginInfo1 = new LoginInfo();
        loginInfo1.setEmail("test@nju.edu.cn");
        loginInfo1.setPassword("123456");
        archiveService.delete(loginInfo1, "4",
                codeHolder1, messageHolder1, dataHolder1);

        System.out.printf("[Delete-Test-1] delete statusCode: %s, message: %s, data: %s%n", codeHolder1.value, messageHolder1.value, dataHolder1.value.toString());
        assert codeHolder1.value.equals(BigInteger.valueOf(StatusCode.SUCCESS.getCode()));

        /* Test Case 2: operator's role must be teacher */

        Holder<BigInteger> codeHolder2 = new Holder<>();
        Holder<String> messageHolder2 = new Holder<>();
        Holder<Boolean> dataHolder2 = new Holder<>();
        LoginInfo loginInfo2 = new LoginInfo();
        loginInfo2.setEmail("201250182@smail.nju.edu.cn");
        loginInfo2.setPassword("123456");
        archiveService.delete(loginInfo2, "4",
                codeHolder2, messageHolder2, dataHolder2);

        System.out.printf("[Delete-Test-2] delete statusCode: %s, message: %s, data: %s%n", codeHolder2.value, messageHolder2.value, dataHolder2.value.toString());
        assert codeHolder2.value.equals(BigInteger.valueOf(StatusCode.NO_ENOUGH_AUTH.getCode()));

        /* Test Case 3: delete non-exist student info */

        Holder<BigInteger> codeHolder3 = new Holder<>();
        Holder<String> messageHolder3 = new Holder<>();
        Holder<Boolean> dataHolder3 = new Holder<>();
        LoginInfo loginInfo3 = new LoginInfo();
        loginInfo3.setEmail("test@nju.edu.cn");
        loginInfo3.setPassword("123456");
        archiveService.delete(loginInfo3, "100",
                codeHolder3, messageHolder3, dataHolder3);

        System.out.printf("[Delete-Test-1] update statusCode: %s, message: %s, data: %s%n", codeHolder3.value, messageHolder3.value, dataHolder3.value.toString());
        assert codeHolder3.value.equals(BigInteger.valueOf(StatusCode.STUDENT_NOT_FOUND.getCode()));

        System.out.println();
    }
}
