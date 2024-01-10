package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 课程成绩单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement(name = "courseGradeList", namespace = "educational-admin")
public class CourseGrade {

    private CourseInfo courseInfo;
    /**
     * 该学生的所有课程成绩
     */
    private List<CourseStudentGrade> studentGrades;
}
