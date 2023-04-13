package com.developers.member.badge.dto.response;

import com.developers.member.member.dto.response.MemberIdResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberSaveBadgeResponse {
    private String code;
    private String msg;
    private MemberIdResponse data;
}
