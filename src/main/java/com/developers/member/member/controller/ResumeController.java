package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberResumeSaveRequest;
import com.developers.member.member.dto.response.MemberResumeSaveResponse;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<MemberResumeSaveResponse> saveMemberResume(@Valid @RequestBody MemberResumeSaveRequest request) {
        MemberResumeSaveResponse response = memberService.saveMemberResume(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
