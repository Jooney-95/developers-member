package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberLoginRequest;
import com.developers.member.member.dto.request.UpdateMemberRefreshRequest;
import com.developers.member.member.dto.response.MemberInfoResponse;
import com.developers.member.member.dto.response.MemberLoginResponse;
import com.developers.member.member.dto.response.UpdateMemberRefreshResponse;
import com.developers.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    /**
     * 사용자 닉네임 정보 전달
     * @param memberId 사용자 PK 번호
     * @return MemberInfoResponse
     */
    @GetMapping("/member")
    public ResponseEntity<MemberInfoResponse> getWriterInfo(@RequestParam Long memberId) {
        MemberInfoResponse response = memberService.getWriterInfo(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 멘토 사용자 닉네임 정보 전달 - 멘토 등록된 사용자의 정보를 전달
     * @param mentorId 사용자 PK 번호
     * @return MemberInfoResponse
     */
    @GetMapping("/mentor")
    public ResponseEntity<MemberInfoResponse> getMentorInfo(@RequestParam Long mentorId) {
        MemberInfoResponse response = memberService.getMentorInfo(mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 로그인 사용자 조회 - Gateway 서비스의 시큐리티에서 로그인 하기 위한 사용자 정보를 전달
     * @param email 로그인한 사용자의 이메일 정보
     * @return MemberLoginResponse
     */
    @GetMapping("/member/email")
    public ResponseEntity<MemberLoginResponse> getLoginMember(@RequestParam String email) {
        MemberLoginResponse response = memberService.getLoginMember(email);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 로그인 성공한 사용자 리프레쉬 토큰 발급 - Gateway 서비스에서 로그인 성공시 해당 사용자 리프레쉬 토큰 갱신
     * @param request 로그인한 사용자의 이메일 정보, 갱신할 리프레쉬 토큰 값
     * @return UpdateMemberRefreshResponse API 상태 값
     */
    @PostMapping("/member/refresh")
    public ResponseEntity<UpdateMemberRefreshResponse> updateRefreshToken(@RequestBody UpdateMemberRefreshRequest request) {
        UpdateMemberRefreshResponse response = memberService.updateRefreshToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
