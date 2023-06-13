package com.zyinnju.backend.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 通讯录 DTO 类
 *
 * @author zhengyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfo {

    /**
     * 名
     */
    @NotEmpty
    private String firstName;
    /**
     * 姓
     */
    @NotEmpty
    private String lastName;
    /**
     * 手机号
     */
    @Length(min = 11, max = 11, message = "请输入11位电话号码")
    private String phone;
    /**
     * 邮箱
     */
    @Email(regexp = "^(.+)@(\\S+)$", message = "请输入合法邮箱")
    private String email;
}
