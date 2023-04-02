package com.developers.member.member.dto.response;

import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Builder;
import lombok.Getter;

/**
 * 작성자 PK 번호 및 닉네임 정보 응답 DTO
 */
@Getter
public class MemberIdWithNicknameResponse {
    private Long memberId;
    private String nickname;

    @Builder
    public MemberIdWithNicknameResponse(Long memberId, String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }
}
