package com.rainer.cloudmall.authentication.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginVo {
    @NotBlank(message = "用户名必须填写")
    private String loginacct;

    @NotBlank(message = "密码必须填写")
    private String password;
}
