package cn.edu.nju.software.utils;

import cn.edu.nju.software.entity.*;
import cn.hutool.core.util.RandomUtil;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    private static final String[] COURSES = {"Machine Learning",
            "Big Data Analysis",
            "Cloud Native",
            "Software Engineering and Computing I",
            "Software Engineering and Computing II"};

    private static final String[] NAMES = {"Adam", "Andrew", "Anthony", "Benjamin",
            "Brandon", "David", "Edward", "Franklin", "George", "Harry", "Isaac",
            "Jack", "James", "John", "Jonathan", "Kirk", "Logan", "Mark", "Nathan",
            "Oscar", "Peter", "Quentin", "Robert", "Samuel", "Thomas", "Timothy",
            "Ulysses", "Victor", "William", "Xavier", "Yancey", "Zachary",
            "Alice", "Betty", "Catherine", "Dorothy", "Emily", "Fiona", "Gloria",
            "Helen", "Isabella", "Jennifer", "Katherine", "Linda", "Mary",
            "Olivia", "Paula", "Quinn", "Ruby", "Sophia", "Tracy", "Ursula",
            "Vanessa", "Wendy", "Xenia", "Yvonne", "Zoe", "Victoria"
    };

    private static final String[] MAJORS = {"AI", "Computer Science", "Software Engineering",
            "Math", "Astronomy", "Physics", "Law", "Geography", "Finance"};

    private DataUtils() throws InstantiationException {
        throw new InstantiationException("can't instantiate this class");
    }

    public static List<StudentInfo> generateStudentInfos(int amount) {
        List<StudentInfo> studentInfos = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setId(String.valueOf(RandomUtil.randomInt(201250002, 201251000)));
            studentInfo.setName(RandomUtil.randomEle(NAMES));
            studentInfo.setAge(RandomUtil.randomInt(18, 24));
            studentInfo.setGender(RandomUtil.randomEle(new String[]{"female", "male"}));
            studentInfo.setMajor(RandomUtil.randomEle(MAJORS));
            studentInfo.setEnrollYear(RandomUtil.randomInt(1990, 2024));
            studentInfos.add(studentInfo);
        }

        return studentInfos;
    }

    public static List<StudentGrade> generateStudentGrades(List<StudentInfo> studentInfos) {
        List<StudentGrade> studentGrades = new ArrayList<>();
        for (StudentInfo studentInfo : studentInfos) {
            StudentGrade studentGrade = new StudentGrade();
            studentGrade.setStudentInfo(studentInfo);
            studentGrade.setCourseGrades(generateCourseGrades());
            studentGrades.add(studentGrade);
        }

        return studentGrades;
    }

    private static List<StudentCourseGrade> generateCourseGrades() {
        List<StudentCourseGrade> studentCourseGrades = new ArrayList<>();
        for (String course : COURSES) {
            StudentCourseGrade studentCourseGrade = new StudentCourseGrade();
            studentCourseGrade.setCourseInfo(CourseInfo.builder()
                    .courseName(course)
                    .build());
            double usualGrade = RandomUtil.randomDouble(0, 40, 1, RoundingMode.HALF_UP);
            double finalGrade = RandomUtil.randomDouble(0, 60, 1, RoundingMode.HALF_UP);
            double totalGrade = usualGrade + finalGrade;
            studentCourseGrade.setGrade(Grade.builder()
                    .usualGrade(usualGrade)
                    .finalGrade(finalGrade)
                    .totalGrade(totalGrade)
                    .build());
            studentCourseGrades.add(studentCourseGrade);
        }

        return studentCourseGrades;
    }
}
