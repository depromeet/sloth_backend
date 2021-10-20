package com.sloth.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sloth.api.oauth.dto.SocialType;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.domain.BaseEntity;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.dto.MemberFormDto;
import com.sloth.domain.memberToken.MemberToken;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "Member.withAll", attributeNodes = {
        @NamedAttributeNode(value = "lessons", subgraph = "lessons")
},
        subgraphs = @NamedSubgraph(name = "lessons", attributeNodes = {
                @NamedAttributeNode("site"),
                @NamedAttributeNode("category")
        })
)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<Lesson> lessons = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberToken memberToken;

    public static Member createAdmin(MemberFormDto memberFormDto) {
        return Member.builder()
                .memberName(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .socialType(memberFormDto.getSocialType())
                .password(memberFormDto.getPassword())
                .role(Role.ADMIN)
                .lessons(new ArrayList<>())
                .build();
    }

    public static Member createOauthMember(OAuthAttributes oAuthAttributes) {
        return Member.builder()
                .memberName(oAuthAttributes.getName())
                .email(oAuthAttributes.getEmail())
                .socialType(oAuthAttributes.getSocialType())
                .password(oAuthAttributes.getPassword())
                .lessons(new ArrayList<>())
                .role(Role.USER)
                .build();
    }

    @JsonIgnore
    public String getRoleKey() {
        return this.role.getKey();
    }

    public void update(String name) {
        this.memberName = name;
    }

}
