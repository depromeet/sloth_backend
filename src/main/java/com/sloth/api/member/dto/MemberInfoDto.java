package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@ApiModel(value = "회원 상세 정보 객체", description = "회원 상세 정보 객체")
public class MemberInfoDto {

    private Long memberId;
    private String memberName;
    private String email;
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
