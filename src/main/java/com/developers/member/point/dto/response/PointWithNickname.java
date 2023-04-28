package com.developers.member.point.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PointWithNickname {
    private Long point;
    private String nickname;
}
