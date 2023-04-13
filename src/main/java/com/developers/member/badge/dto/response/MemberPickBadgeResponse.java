package com.developers.member.badge.dto.response;

import com.developers.member.badge.entity.MyBadge;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MemberPickBadgeResponse {
    private String code;
    private String msg;
    private MemberIdWithPickBadgeResponse data;

}
