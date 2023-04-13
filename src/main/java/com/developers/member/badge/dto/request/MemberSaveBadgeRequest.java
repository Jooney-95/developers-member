package com.developers.member.badge.dto.request;

import com.developers.member.badge.entity.Badges;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberSaveBadgeRequest {
    private Long memberId;
    private Badges archieveBadge;
}
