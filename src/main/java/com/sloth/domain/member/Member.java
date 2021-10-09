package com.sloth.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sloth.domain.BaseEntity;
import com.sloth.domain.EntityCommonMethod;
import com.sloth.domain.member.constant.Role;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.sloth.domain.member.dto.MemberFormDto;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET is_delete = true WHERE member_id=?")
@Where(clause = "is_delete=false")
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column
    private String picture;

    @JsonIgnore
    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    private boolean isDelete = false;

    public static Member createAdmin(MemberFormDto memberFormDto) {
        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .address(memberFormDto.getAddress())
                .password(memberFormDto.getPassword())
                .role(Role.USER)
                .build();
    }

    public Member update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }

    @JsonIgnore
    public String getRoleKey() {
        return this.role.getKey();
    }

}
