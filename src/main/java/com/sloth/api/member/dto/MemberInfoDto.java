package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@ApiModel(value = "회원 상세 정보 객체", description = "회원 상세 정보 객체")
public class MemberInfoDto {

    @ApiModelProperty(value = "회원 아이디", example = "1")
    private Long memberId;
    @ApiModelProperty(value = "회원 이름", example = "홍길동")
    private String memberName;
    @ApiModelProperty(value = "이메일", example = "test@test.com")
    private String email;
    @ApiModelProperty(value = "이메일 인증 여부", example = "true")
    private Boolean isEmailProvided;

    public MemberInfoDto(Member entity) {
        this.memberId = entity.getMemberId();
        this.memberName = entity.getMemberName();
        this.email = entity.getEmail();
        this.isEmailProvided = checkEmailProvided(entity.getEmail());
    }

    private Boolean checkEmailProvided(String email) {
        String regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";

        return Pattern.matches(regex, email);
    }
}
