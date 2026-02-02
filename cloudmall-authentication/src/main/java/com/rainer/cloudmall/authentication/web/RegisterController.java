package com.rainer.cloudmall.authentication.web;

import com.rainer.cloudmall.authentication.service.AuthService;
import com.rainer.cloudmall.authentication.vo.UserRegisterVo;
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
public class RegisterController {
    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterVo userRegisterVo,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes
    ) {
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
            return "redirect:http://auth.cloudmall.com/reg.html";
        }

        return authService.register(userRegisterVo, redirectAttributes);
    }
}
