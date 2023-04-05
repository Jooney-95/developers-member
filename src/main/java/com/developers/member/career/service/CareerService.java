package com.developers.member.career.service;

import com.developers.member.career.dto.request.MemberCareerSaveRequest;
import com.developers.member.career.dto.request.MemberCareerUpdateRequest;
import com.developers.member.career.dto.response.MemberCareerGetResponse;
import com.developers.member.career.dto.response.MemberCareerResponse;

public interface CareerService {
    MemberCareerGetResponse getCareerInfo(Long memberId);

    MemberCareerResponse saveCareer(MemberCareerSaveRequest request);

    MemberCareerResponse updateCareer(MemberCareerUpdateRequest request);

    MemberCareerResponse removeCareer(Long careerId);
}
