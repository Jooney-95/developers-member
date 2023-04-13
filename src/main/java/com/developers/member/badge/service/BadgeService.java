package com.developers.member.badge.service;

import com.developers.member.badge.dto.request.MemberChangeBadgeRequest;
import com.developers.member.badge.dto.request.MemberSaveBadgeRequest;
import com.developers.member.badge.dto.response.MemberChangeBadgeResponse;
import com.developers.member.badge.dto.response.MemberAllBadgeResponse;
import com.developers.member.badge.dto.response.MemberPickBadgeResponse;
import com.developers.member.badge.dto.response.MemberSaveBadgeResponse;

public interface BadgeService {
    MemberAllBadgeResponse getMyAllBadge(Long memberId);
    MemberPickBadgeResponse getMyPickBadge(Long memberId);

    MemberChangeBadgeResponse changeBadge(MemberChangeBadgeRequest request);

    MemberSaveBadgeResponse saveBadgeList(MemberSaveBadgeRequest request);
}
