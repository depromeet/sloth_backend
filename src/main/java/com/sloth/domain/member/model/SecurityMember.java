package com.sloth.domain.member.model;

import lombok.Getter;
import lombok.Setter;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SecurityMember extends User {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final long serialVersionUID = 1L;

    private Member member;

    public SecurityMember(Member member) {
        super(member.getEmail(), member.getPassword(), makeGrantedAuthority(member.getRole()));
        this.member = member;
    }

    private static List<GrantedAuthority> makeGrantedAuthority(Role role){
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        return list;
    }
}