package com.sloth.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.global.config.auth.dto.OAuthAttributes;
import com.sloth.domain.common.BaseEntity;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.memberToken.MemberToken;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="member")
@Getter @Setter
@ToString(exclude = {"memberToken","lessons"})
@Builder
@AllArgsConstructor @NoArgsConstructor
@SQLDelete(sql = "UPDATE member SET is_delete = true WHERE member_id=?")
@Where(clause = "is_delete=false")
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false)
    private String memberName;

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

    @JsonIgnore
    @Column
    private String emailConfirmCode;

    @JsonIgnore
    @Column
    private LocalDateTime emailConfirmCodeCreatedAt;

    @JsonIgnore
    @Column
    private boolean isEmailConfirm = true;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Lesson> lessons = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberToken memberToken;


    public static Member createAdmin(MemberFormDto formRequestDto) {
        return Member.builder()
                .memberName(formRequestDto.getMemberName())
                .email(formRequestDto.getEmail())
                .socialType(formRequestDto.getSocialType())
                .password(formRequestDto.getPassword())
                .lessons(new ArrayList<>())
                .isEmailConfirm(true)
                .role(Role.ADMIN)
                .build();
    }

    public static Member createOauthMember(OAuthAttributes oAuthAttributes, String name) {
        return Member.builder()
                .memberName(name)
                .email(oAuthAttributes.getEmail())
                .socialType(oAuthAttributes.getSocialType())
                .password(oAuthAttributes.getPassword())
                .lessons(new ArrayList<>())
                .isEmailConfirm(true)
                .role(Role.USER)
                .build();
    }

    public static Member createFormMember(FormJoinDto formRequestDto, String encodedPassword) {
        return Member.builder()
                .memberName(formRequestDto.getMemberName())
                .email(formRequestDto.getEmail())
                .socialType(SocialType.FORM)
                .password(encodedPassword)
                .isEmailConfirm(false)
                .lessons(new ArrayList<>())
                .role(Role.USER)
                .build();
    }

    @JsonIgnore
    public String getRoleKey() {
        return this.role.getKey();
    }

    public void updateMemberName(String name) {
        this.memberName = name;
    }

    public boolean confirmEmail(String confirmCode) {
        return confirmCode.equals(this.getEmailConfirmCode());
    }

    public void activate() {
        this.isEmailConfirm = true;
    }

    public void updateMemberToken(MemberToken memberToken) {
        this.memberToken = memberToken;
    }

    public void updateConfirmEmailCode(String emailConfirmCode, LocalDateTime now) {
        this.emailConfirmCode = emailConfirmCode;
        this.emailConfirmCodeCreatedAt = now;
    }

    public boolean canCreateEmailConfirmCode(LocalDateTime now) {
        return this.emailConfirmCodeCreatedAt.isBefore(now.minusMinutes(5)) ||
                this.emailConfirmCodeCreatedAt.isEqual(now.minusMinutes(5));
    }
}
