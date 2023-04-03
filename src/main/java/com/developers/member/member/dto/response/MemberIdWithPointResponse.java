package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 작성자 PK 정보 및 점수 응답 DTO
 */
@Builder
@ToString
@Getter
public class MemberIdWithPointResponse {
    private Long memberId;
    private Long point;
}
