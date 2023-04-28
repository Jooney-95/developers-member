package com.developers.member.point.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter

public class PointRanking {
    private List<PointWithNickname> pointRanking;
}
