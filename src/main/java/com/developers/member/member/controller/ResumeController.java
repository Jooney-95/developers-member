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
@RequestMapping("/api/member")
public class ResumeController {
    private final MemberService memberService;

    /**
     * 사용자 이력정보 등록
     * 간단소개글, 주요기술, 직무 3가지를 한번에 등록.
     * @param request 사용자 PK 번호, 간단소개글, 주요기술, 직무 정보
     * @return MemberResumeSaveResponse
     */
    @PatchMapping("/resume")
    public ResponseEntity<MemberResumeSaveResponse> saveMemberResume(@Valid @RequestBody MemberResumeSaveRequest request) {
        MemberResumeSaveResponse response = memberService.saveMemberResume(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
