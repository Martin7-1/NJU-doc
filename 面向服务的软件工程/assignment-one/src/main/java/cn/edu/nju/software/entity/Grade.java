package cn.edu.nju.software.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    private Double usualGrade;
    private Double finalGrade;
    private Double totalGrade;
}
