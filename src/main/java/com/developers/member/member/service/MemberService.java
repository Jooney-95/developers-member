package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberRegisterResponse;

public interface MemberService {
    MemberRegisterResponse register(MemberRegisterRequest request);
}
