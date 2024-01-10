package cn.edu.nju.software.soa.login.enums;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "Role", namespace = "http://login.soa.software.nju.edu.cn/schema/")
public enum Role {

    /**
     * 本科生
     */
    UNDERGRADUATE,
    /**
     * 研究生
     */
    GRADUATE,
    /**
     * 老师
     */
    TEACHER
}
