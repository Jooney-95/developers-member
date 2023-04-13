package com.developers.member.badge.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberChangeBadgeRequest {
    private Long memberId;
    private String badgeName;
}
