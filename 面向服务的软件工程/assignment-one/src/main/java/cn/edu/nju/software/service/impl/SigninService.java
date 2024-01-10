package cn.edu.nju.software.service.impl;

import cn.edu.nju.software.utils.ValidationUtils;

public class SigninService {

    private static final String RESOURCES_PATH = "/sign-in/";
    private static final String XML_FILE = RESOURCES_PATH + "StudentInfo.xml";
    private static final String SCHEMA_FILE = RESOURCES_PATH + "StudentInfo.xsd";

    public static void main(String[] args) {
        // 读取实例并验证
        System.out.println(ValidationUtils.validate(SCHEMA_FILE, XML_FILE));
    }
}
