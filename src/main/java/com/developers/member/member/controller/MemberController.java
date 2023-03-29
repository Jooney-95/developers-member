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

    @GetMapping("/member")
    public ResponseEntity<MemberInfoResponse> getWriterInfo(@RequestParam Long memberId) {
        System.out.println(memberId);
        MemberInfoResponse response = memberService.getWriterInfo(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/mentor")
    public ResponseEntity<MemberInfoResponse> getMentorInfo(@RequestParam Long mentorId) {
        MemberInfoResponse response = memberService.getMentorInfo(mentorId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
