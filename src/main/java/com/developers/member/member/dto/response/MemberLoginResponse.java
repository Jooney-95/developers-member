package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MemberLoginResponse {
    private String loginEmail;
    private String loginPassword;
}

