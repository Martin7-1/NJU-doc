package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 学生成绩单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement(name = "studentGrade", namespace = "educational-admin")
public class StudentGrade {

    private StudentInfo studentInfo;
    /**
     * 该学生的所有课程成绩
     */
    private List<StudentCourseGrade> courseGrades;
}
