package cn.edu.nju.software.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "courseGradeList", namespace = "educational-admin")
@XmlAccessorType(XmlAccessType.NONE)
public class CourseGradeList {

    @XmlElement(name = "courseGrade", namespace = "educational-admin")
    private List<CourseGrade> courseGrades;

    public CourseGradeList(List<CourseGrade> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public CourseGradeList() {

    }

    public void setCourseGrades(List<CourseGrade> courseGrades) {
        this.courseGrades = courseGrades;
    }
}
