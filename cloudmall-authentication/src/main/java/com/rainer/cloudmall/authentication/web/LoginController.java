package com.rainer.cloudmall.authentication.web;

import com.rainer.cloudmall.authentication.service.AuthService;
import com.rainer.cloudmall.authentication.vo.UserLoginVo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class LoginController {
    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginVo loginVo, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "格式错误",
                            (v1, v2) -> v1
                    ));
            log.warn(errors.toString());
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.cloudmall.com/login.html";
        }

        return authService.login(loginVo, redirectAttributes, session);
    }
}
