package com.developers.member.member.controller;

import com.developers.member.member.dto.request.NicknameUpdateRequest;
import com.developers.member.member.dto.request.PasswordChangeRequest;
import com.developers.member.member.dto.request.ProfileImageUpdateRequest;
import com.developers.member.member.dto.response.NicknameUpdateResponse;
import com.developers.member.member.dto.response.PasswordChangeResponse;
import com.developers.member.member.dto.response.ProfileGetResponse;
import com.developers.member.member.dto.response.ProfileImageUpdateResponse;
import com.developers.member.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProfileController {
    private final MemberService memberService;

    /**
     * 개인정보 조회
     * @param memberId 사용자 PK 번호
     * @return ProfileGetResponse
     */
    @GetMapping("/profile/{memberId}")
    public ResponseEntity<ProfileGetResponse> getProfile(@Valid @PathVariable Long memberId) {
        ProfileGetResponse response = memberService.getProfile(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 닉네임 변경
     * @param request 사용자의 PK 번호, 변경할 닉네임 정보
     * @return NicknameUpdateResponse
     */
    @PatchMapping("/nickname")
    public ResponseEntity<NicknameUpdateResponse> updateNickname(@Valid @RequestBody NicknameUpdateRequest request) {
        NicknameUpdateResponse response = memberService.updateNickname(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 프로필 이미지 변경
     * @param request 사용자의 PK 번호, 변경할 이미지 경로
     * @return ProfileImageUpdateResponse
     */

    @PatchMapping("/profileimg")
    public ResponseEntity<ProfileImageUpdateResponse> updateProfileImg(@Valid @RequestBody ProfileImageUpdateRequest request) {
        ProfileImageUpdateResponse response = memberService.updateProfileImg(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 비밀번호 변경
     * @param request 사용자의 PK 번호, 변경할 비밀번호
     * @return PasswordChangeResponse
     */

    @PatchMapping("/password")
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        PasswordChangeResponse response = memberService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
