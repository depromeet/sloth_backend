package com.sloth.api.login.validator;

import com.sloth.api.login.dto.FormJoinDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormRegisterValidator implements Validator {

    private static final String emailRegExp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;

    public FormRegisterValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FormJoinDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormJoinDto formRequestDto = (FormJoinDto) target;

        if (formRequestDto.getEmail() == null || formRequestDto.getEmail().trim().isEmpty()) {
            errors.rejectValue("email","required","필수 입력 정보입니다.");
        } else {
            Matcher matcher = pattern.matcher(formRequestDto.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email","bad","올바르지 않은 형식입니다.");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "memberName","필수 입력 정보입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","필수 입력 정보입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm","필수 입력 정보입니다.");

        if (!StringUtils.isEmpty(formRequestDto.getPassword())) {
            if (formRequestDto.getPassword().length() <= 7 || formRequestDto.getPassword().length() > 16) {
                errors.rejectValue("password", "bad","비밀번호는 8자 이상, 16자 이하로 입력해주세요.");
            }
            if (!formRequestDto.getPassword().equals(formRequestDto.getPasswordConfirm())) {
                errors.rejectValue("passwordConfirm","noMatch","비밀번호가 일치하지 않습니다.");
            }
        }

    }
}
