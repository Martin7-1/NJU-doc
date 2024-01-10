package cn.edu.nju.software.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "studentGradeList", namespace = "educational-admin")
@XmlAccessorType(XmlAccessType.NONE)
public class StudentGradeList {

    @XmlElement(name = "studentGrade", namespace = "educational-admin")
    private List<StudentGrade> studentGrades;

    public StudentGradeList(List<StudentGrade> studentGrades) {
        this.studentGrades = studentGrades;
    }

    public StudentGradeList() {

    }

    public void setStudentGrades(List<StudentGrade> studentGrades) {
        this.studentGrades = studentGrades;
    }
}
