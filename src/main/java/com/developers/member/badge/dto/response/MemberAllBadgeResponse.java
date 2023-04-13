package com.developers.member.badge.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MemberAllBadgeResponse {
    private String code;
    private String msg;
    private MemberIdWithAllBadgeResponse data;
}
