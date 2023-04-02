package com.developers.member.member.dto.response;

import com.developers.member.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

/**
 * 개인정보 조회 응답 DTO
 */
@Builder
@Getter
public class ProfileGetResponse {
    private String code;
    private String msg;
    private ProfileResponse data;
}
