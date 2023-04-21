package com.developers.member.member.service;

import com.developers.member.member.dto.request.*;
import com.developers.member.member.dto.response.*;

public interface MemberService {
    MemberRegisterResponse register(MemberRegisterRequest request);

    MemberInfoResponse getWriterInfo(Long memberId);

    MemberInfoResponse getMentorInfo(Long mentorId);


    ProfileGetResponse getProfile(Long memberId);

    NicknameUpdateResponse updateNickname(NicknameUpdateRequest request);

    ProfileImageUpdateResponse updateProfileImg(ProfileImageUpdateRequest request);

    PasswordChangeResponse changePassword(PasswordChangeRequest request);

    MemberResumeSaveResponse saveMemberResume(MemberResumeSaveRequest request);

    MemberLoginResponse getLoginMember(String email);

    UpdateMemberRefreshResponse updateRefreshToken(UpdateMemberRefreshRequest request);

    AddressUpdateResponse updateAddress(AddressUpdateRequest request);

    MemberRemoveResponse removeMember(Long memberId);

    MentorRegisterResponse registerMentor(MentorRegisterReqeust request);
}
