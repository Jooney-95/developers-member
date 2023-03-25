package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 작성자 PK 정보 응답 DTO
 */
@Getter
public class MemberIdResponse {
    private Long memberId;
    @Builder
    public MemberIdResponse(Long memberId) {
        this.memberId = memberId;
    }
}
