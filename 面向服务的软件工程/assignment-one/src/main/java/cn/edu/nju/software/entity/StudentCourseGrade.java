package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学生的课程成绩
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentCourseGrade {

    private CourseInfo courseInfo;
    private Grade grade;
}
