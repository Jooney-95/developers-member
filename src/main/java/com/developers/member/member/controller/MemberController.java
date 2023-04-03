package com.developers.member.member.controller;

import com.developers.member.member.dto.response.MemberInfoResponse;
import com.developers.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
