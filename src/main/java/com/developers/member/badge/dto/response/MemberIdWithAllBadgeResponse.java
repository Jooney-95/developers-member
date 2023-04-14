package com.developers.member.badge.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class MemberIdWithAllBadgeResponse {
    private Long memberId;
    private List<MyBadgeList> myBadgeList;
}
