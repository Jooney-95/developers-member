package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.request.NicknameUpdateRequest;
import com.developers.member.member.dto.request.PasswordChangeRequest;
import com.developers.member.member.dto.request.ProfileImageUpdateRequest;
import com.developers.member.member.dto.response.*;

public interface MemberService {
    MemberRegisterResponse register(MemberRegisterRequest request);

    MemberInfoResponse getWriterInfo(Long memberId);

    MemberInfoResponse getMentorInfo(Long mentorId);


    ProfileGetResponse getProfile(Long memberId);

    NicknameUpdateResponse updateNickname(NicknameUpdateRequest request);

    ProfileImageUpdateResponse updateProfileImg(ProfileImageUpdateRequest request);

    PasswordChangeResponse changePassword(PasswordChangeRequest request);
}
