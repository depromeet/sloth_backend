package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@ApiModel(value = "마이페이지 조회 객체", description = "마이페이지 조회 객체")
public class ResponseMemberDto {

    private Long id;
    private String name;
    private String email;

    public  ResponseMemberDto(Member entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
    }
}
