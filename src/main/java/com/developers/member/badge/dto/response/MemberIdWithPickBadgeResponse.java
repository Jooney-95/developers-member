package com.developers.member.badge.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberIdWithPickBadgeResponse {
    private Long memberId;
    private String myBadge;
}
