package com.sloth.domain.member.dto;

import com.sloth.domain.member.constant.SocialType;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {

    @NotEmpty(message = "이름은 필수 입력 값입니다.")
    private String memberName;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "SNS 타입은 필수 입력 값입니다.")
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

}