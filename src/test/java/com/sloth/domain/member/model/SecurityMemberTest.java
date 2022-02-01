package com.sloth.domain.member.model;

import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class SecurityMemberTest {

    @Test
    void test() {

        // given
        Member member = MemberCreator.createMember(1L, "test@test.com");

        // when
        SecurityMember securityMember = new SecurityMember(member);

        // then
        Assertions.assertThat(securityMember.getUsername()).isEqualTo(member.getEmail());
        Assertions.assertThat(securityMember.getPassword()).isEqualTo(member.getPassword());
        securityMember.getAuthorities().stream().forEach(authority -> {
            Assertions.assertThat(authority.getAuthority()).isEqualTo("ROLE_" + member.getRole());
        });
    }

}