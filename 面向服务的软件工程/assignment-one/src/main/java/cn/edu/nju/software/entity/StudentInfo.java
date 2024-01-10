package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentInfo {

    private String id;
    private String name;
    private Integer age;
    private String gender;
    private String major;
    private Integer enrollYear;
}
