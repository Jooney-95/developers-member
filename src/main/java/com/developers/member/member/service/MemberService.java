package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.dto.response.MemberInfoResponse;

public interface MemberService {
    MemberRegisterResponse register(MemberRegisterRequest request);

    MemberInfoResponse getWriterInfo(Long memberId);

    MemberInfoResponse getMentorInfo(Long mentorId);


}
