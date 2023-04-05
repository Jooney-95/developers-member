package com.developers.member.career.controller;

import com.developers.member.career.dto.request.MemberCareerUpdateRequest;
import com.developers.member.career.dto.response.MemberCareerGetResponse;
import com.developers.member.career.dto.request.MemberCareerSaveRequest;
import com.developers.member.career.dto.response.MemberCareerResponse;
import com.developers.member.career.service.CareerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/career")
public class CareerController {
    private final CareerService careerService;

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberCareerGetResponse> getCareerInfo(@PathVariable Long memberId) {
        MemberCareerGetResponse response = careerService.getCareerInfo(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<MemberCareerResponse> saveCareer(@Valid @RequestBody MemberCareerSaveRequest request) {
        MemberCareerResponse response = careerService.saveCareer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping
    public ResponseEntity<MemberCareerResponse> updateCareer(@Valid @RequestBody MemberCareerUpdateRequest request) {
        MemberCareerResponse response = careerService.updateCareer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<MemberCareerResponse> removeCareer(@Valid @PathVariable Long careerId) {
        MemberCareerResponse response = careerService.removeCareer(careerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
