package com.developers.member.auth.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberDto extends User {
    private String loginEmail;
    private String loginPassword;

    public MemberDto(String username, String password, Collection<GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.loginEmail = username;
        this.loginPassword = password;
    }
}
