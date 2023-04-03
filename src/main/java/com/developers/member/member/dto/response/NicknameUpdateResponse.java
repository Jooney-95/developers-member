package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 개인정보 닉네임 변경 응답 DTO
 */
@Builder
@Getter
public class NicknameUpdateResponse {
    private String code;
    private String msg;
    private MemberIdWithNicknameResponse data;
}
