package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberRegisterResponse> register(@Valid @RequestBody MemberRegisterRequest request) {
        MemberRegisterResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
