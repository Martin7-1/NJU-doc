package cn.edu.nju.software.service;

import cn.edu.nju.software.entity.StudentInfo;

import java.util.List;

public interface ArchiveManagementService {

    /**
     * 获取学生信息
     *
     * @return 学生信息
     */
    List<StudentInfo> getStudentInfos();
}
