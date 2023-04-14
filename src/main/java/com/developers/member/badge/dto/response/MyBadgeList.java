package com.developers.member.badge.dto.response;

import com.developers.member.badge.entity.Badges;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class MyBadgeList {
    private String badge;
}
