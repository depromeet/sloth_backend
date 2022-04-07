package com.sloth.api.login.form.validator;

import com.sloth.api.login.form.dto.FormLoginRequestDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormLoginValidator implements Validator {

    private static final String emailRegExp =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final Pattern pattern;

    public FormLoginValidator() {
        pattern = Pattern.compile(emailRegExp);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FormLoginRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormLoginRequestDto formRequestDto = (FormLoginRequestDto) target;

        if (formRequestDto.getEmail() == null || formRequestDto.getEmail().trim().isEmpty()) {
            errors.rejectValue("email","required","필수 입력 정보입니다.");
        } else {
            Matcher matcher = pattern.matcher(formRequestDto.getEmail());
            if (!matcher.matches()) {
                errors.rejectValue("email","bad","올바르지 않은 형식입니다.");
            }
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","필수 입력 정보입니다.");

    }
}
