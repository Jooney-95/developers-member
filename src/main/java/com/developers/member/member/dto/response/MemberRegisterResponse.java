package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원가입 API 응답 DTO
 */
@NoArgsConstructor
@ToString
@Getter
public class MemberRegisterResponse {
    private String code;
    private String msg;
    private MemberIdResponse data;

    @Builder
    public MemberRegisterResponse(String code, String msg, MemberIdResponse data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
