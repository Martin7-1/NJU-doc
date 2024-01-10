package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程的学生成绩
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseStudentGrade {

    private StudentInfo studentInfo;
    private Grade grade;
}
