package com.developers.member.badge.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberChangeBadgeResponse {
    private String code;
    private String msg;
    private MemberIdWithPickBadgeResponse data;
}
