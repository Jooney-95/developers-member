package com.developers.member.point.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetPointRankingResponse {
    private String code;
    private String msg;
    private PointRanking data;
}
