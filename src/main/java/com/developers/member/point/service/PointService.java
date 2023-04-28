package com.developers.member.point.service;

import com.developers.member.point.dto.request.MemberPointRequest;
import com.developers.member.point.dto.response.GetPointRankingResponse;
import com.developers.member.point.dto.response.MemberPointResponse;

public interface PointService {
    MemberPointResponse increasePoint(MemberPointRequest request);

    MemberPointResponse decreasePoint(MemberPointRequest request);

    GetPointRankingResponse getPointRanking();
}
