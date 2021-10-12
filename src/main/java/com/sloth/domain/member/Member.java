package com.sloth.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sloth.api.oauth.dto.SocialType;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.domain.BaseEntity;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.memberToken.MemberToken;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;

@Entity
@Table(name="member")
@Getter
@Setter
@ToString(exclude = "memberToken")
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

    @Column(nullable = false)
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SocialType socialType;

    @Column
    private String picture;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @JsonIgnore
    private boolean isDelete = false;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberToken memberToken;

    public static Member createAdmin(MemberFormDto memberFormDto) {
        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .socialType(memberFormDto.getSocialType())
                .password(memberFormDto.getPassword())
                .role(Role.ADMIN)
                .build();
    }

    public static Member createOauthMember(OAuthAttributes oAuthAttributes) {
        return Member.builder()
                .name(oAuthAttributes.getName())
                .email(oAuthAttributes.getEmail())
                .socialType(oAuthAttributes.getSocialType())
                .password(oAuthAttributes.getPassword())
                .role(Role.USER)
                .build();
    }

    @JsonIgnore
    public String getRoleKey() {
        return this.role.getKey();
    }

}
