package com.rainer.cloudmall.authentication.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterVo {
    @NotBlank(message = "用户名必须提交")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,18}$", message = "用户名必须是4-18位字符，只能包含字母、数字、_和-")
    private String userName;
    @NotBlank(message = "密码必须填写")
    @Pattern(regexp = "^(?!\\d+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$", message = "密码必须是6-20位的数字和字母组合")
    private String password;
    @NotBlank(message = "手机号必须填写")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    @NotBlank(message = "验证码必须填写")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    private String code;
}
