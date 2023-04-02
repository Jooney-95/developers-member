package com.developers.member.member.dto.request;

import lombok.Builder;
import lombok.Getter;

/**
 * 닉네임을 변경하기 위한 요청 DTO
 */
@Getter
@Builder
public class NicknameUpdateRequest {
    private Long memberId;
    private String nickname;
}
