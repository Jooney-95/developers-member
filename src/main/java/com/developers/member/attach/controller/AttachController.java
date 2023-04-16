package com.developers.member.attach.controller;

import com.developers.member.attach.dto.response.MemberProfileImgUpdateResponse;
import com.developers.member.attach.service.AttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attach")
public class AttachController {
    private final AttachService attachService;

    @PostMapping("/profile")
    public ResponseEntity<MemberProfileImgUpdateResponse> uploadProfileImage(
            @RequestParam(value = "memberId") Long memberId,
            @RequestPart(value = "file") MultipartFile multipartFile
    ) {
        MemberProfileImgUpdateResponse response = attachService.uploadProfileImage(memberId, multipartFile);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
