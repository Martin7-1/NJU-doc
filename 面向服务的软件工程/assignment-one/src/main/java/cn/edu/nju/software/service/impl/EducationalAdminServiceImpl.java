package cn.edu.nju.software.service.impl;

import cn.edu.nju.software.entity.*;
import cn.edu.nju.software.service.ArchiveManagementService;
import cn.edu.nju.software.service.EducationalAdminService;
import cn.edu.nju.software.utils.DataUtils;
import cn.edu.nju.software.utils.DocumentUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EducationalAdminServiceImpl implements EducationalAdminService {

    private final ArchiveManagementService archiveManagementService;

    public EducationalAdminServiceImpl(ArchiveManagementService archiveManagementService) {
        this.archiveManagementService = archiveManagementService;
    }

    /**
     * 处理学生成绩单
     */
    @Override
    public void handleStudentGrade() {
        // 获取学生信息
        StudentInfo studentInfo = archiveManagementService.getStudentInfos().get(0);
        // 生成学生信息
        List<StudentInfo> studentInfos = DataUtils.generateStudentInfos(9);
        studentInfos.add(studentInfo);
        // 生成成绩
        List<StudentGrade> studentGrades = DataUtils.generateStudentGrades(studentInfos);
        // 转化为 XML 文档
        DocumentUtils.toXML(new StudentGradeList(studentGrades),
                "src/main/resources/educational-admin/StudentGrades.xml", StudentGradeList.class);
    }

    /**
     * 处理课程成绩单
     */
    @Override
    public void handleCourseGrade() {
        // 读取 StudentGrades.xml
        String documentPath = "src/main/resources/educational-admin/StudentGrades.xml";
        List<StudentGrade> studentGrades;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(documentPath);
            studentGrades = readStudentGradeFromDocument(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 转换为 CourseGrades
        List<CourseGrade> courseGrades = convertStudentGrades2CourseGrades(studentGrades);
        // 按照总评成绩进行排序
        courseGrades.forEach(courseGrade -> courseGrade.getStudentGrades().sort((o1, o2) -> o2.getGrade().getTotalGrade().compareTo(o1.getGrade().getTotalGrade())));

        DocumentUtils.toXML(new CourseGradeList(courseGrades), "src/main/resources/educational-admin/SortedCourseGrades.xml", CourseGradeList.class);

        // 保留任意不及格成绩
        courseGrades.forEach(courseGrade -> courseGrade.setStudentGrades(courseGrade.getStudentGrades().stream().filter(studentGrade -> studentGrade.getGrade().getTotalGrade() < 60).collect(Collectors.toList())));

        DocumentUtils.toXML(new CourseGradeList(courseGrades), "src/main/resources/educational-admin/FailedCourseGrades.xml", CourseGradeList.class);
    }

    private List<StudentGrade> readStudentGradeFromDocument(Document document) {
        List<StudentGrade> studentGrades = new ArrayList<>();
        NodeList nodelist = document.getElementsByTagName("ea:studentGradeList");
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node node = nodelist.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                StudentGrade studentGrade = new StudentGrade();

                // handle studentCourseGrade
                List<StudentCourseGrade> courseGrades = new ArrayList<>();
                NodeList courseGradesNodeList = element.getElementsByTagName("courseGrades");
                for (int j = 0; j < courseGradesNodeList.getLength(); j++) {
                    Node courseGradeNode = courseGradesNodeList.item(j);
                    if (courseGradeNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element courseGradeElement = (Element) courseGradeNode;
                        StudentCourseGrade courseGrade = new StudentCourseGrade();

                        Element courseInfo = (Element) courseGradeElement.getElementsByTagName("courseInfo").item(0);
                        courseGrade.setCourseInfo(new CourseInfo(courseInfo.getElementsByTagName("courseName").item(0).getTextContent()));
                        Element grade = (Element) courseGradeElement.getElementsByTagName("grade").item(0);
                        double usualGrade = Double.parseDouble(grade.getElementsByTagName("usualGrade").item(0).getTextContent());
                        double finalGrade = Double.parseDouble(grade.getElementsByTagName("finalGrade").item(0).getTextContent());
                        double totalGrade = Double.parseDouble(grade.getElementsByTagName("totalGrade").item(0).getTextContent());
                        courseGrade.setGrade(Grade.builder()
                                .usualGrade(usualGrade)
                                .finalGrade(finalGrade)
                                .totalGrade(totalGrade)
                                .build());

                        courseGrades.add(courseGrade);
                    }
                }

                // handle studentInfo
                StudentInfo studentInfo = null;
                Node studentInfoNode = element.getElementsByTagName("studentInfo").item(0);
                if (studentInfoNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentInfoElement = (Element) studentInfoNode;
                    studentInfo = new StudentInfo(studentInfoElement.getElementsByTagName("id").item(0).getTextContent(),
                            studentInfoElement.getElementsByTagName("name").item(0).getTextContent(),
                            Integer.parseInt(studentInfoElement.getElementsByTagName("age").item(0).getTextContent()),
                            studentInfoElement.getElementsByTagName("gender").item(0).getTextContent(),
                            studentInfoElement.getElementsByTagName("major").item(0).getTextContent(),
                            Integer.parseInt(studentInfoElement.getElementsByTagName("enrollYear").item(0).getTextContent())
                    );
                }


                studentGrade.setCourseGrades(courseGrades);
                studentGrade.setStudentInfo(studentInfo);
                studentGrades.add(studentGrade);
            }
        }

        return studentGrades;
    }

    private List<CourseGrade> convertStudentGrades2CourseGrades(List<StudentGrade> studentGrades) {
        List<CourseGrade> targetCourseGrades = new ArrayList<>();
        studentGrades.forEach(studentGrade -> {
            StudentInfo studentInfo = studentGrade.getStudentInfo();
            List<StudentCourseGrade> courseGrades = studentGrade.getCourseGrades();
            courseGrades.forEach(courseGrade -> {
                CourseStudentGrade courseStudentGrade = CourseStudentGrade.builder()
                        .studentInfo(studentInfo)
                        .grade(courseGrade.getGrade())
                        .build();
                if (targetCourseGrades.stream()
                        .noneMatch(targetCourseGrade -> targetCourseGrade.getCourseInfo().getCourseName().equals(courseGrade.getCourseInfo().getCourseName()))) {
                    // 说明此时在结果中还没有这个课程
                    targetCourseGrades.add(CourseGrade.builder()
                            .courseInfo(courseGrade.getCourseInfo())
                            .studentGrades(new ArrayList<>(Collections.singletonList(courseStudentGrade)))
                            .build());
                } else {
                    // 说明在结果中已经有了这个课程
                    targetCourseGrades.stream()
                            .filter(targetCourseGrade -> targetCourseGrade.getCourseInfo().getCourseName().equals(courseGrade.getCourseInfo().getCourseName()))
                            .forEach(targetCourseGrade -> targetCourseGrade.getStudentGrades().add(courseStudentGrade));
                }
            });
        });
        return targetCourseGrades;
    }
}
