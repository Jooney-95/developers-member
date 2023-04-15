package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.dto.response.MemberRemoveResponse;
import com.developers.member.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final MemberService memberService;

    /**
     * 사용자 회원가입
     *
     * @param request 이메일, 비밀번호, 닉네임, 프로필이미지경로, 주소, 직무, 기술스택
     * @return MemberRegisterResponse
     */
    @PostMapping("/register")
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 사용자 회원 탈퇴
     * 회원 탈퇴시 Member, Point, Career 정보를 모두 삭제함.
     * @param memberId 사용자 PK 번호
     * @return MemberRemoveResponse
     */
    @DeleteMapping("/{memberId}")
    public ResponseEntity<MemberRemoveResponse> removeMember(@Valid @PathVariable Long memberId) {
        MemberRemoveResponse response = memberService.removeMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
