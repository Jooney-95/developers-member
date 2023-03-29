package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 사용자(멘토) 닉네임 응답 DTO
 */
@Getter
@Builder
public class MemberInfoResponse {
    private String memberName;
}
