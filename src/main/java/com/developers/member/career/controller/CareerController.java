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
@RequestMapping("/api/member/career")
public class CareerController {
    private final CareerService careerService;

    /**
     * 사용자 경력정보 조회
     * 이력정보에서 간단소개글, 주요기술, 직무와 더불어 보여줄 경력정보 조회
     * @param memberId 사용자 PK 번호
     * @return MemberCareerGetResponse
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberCareerGetResponse> getCareerInfo(@PathVariable Long memberId) {
        MemberCareerGetResponse response = careerService.getCareerInfo(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 경력정보 등록
     * 사용자별로 N개의 이력정보를 등록 가능.
     * @param request 근무처, 근무시작 및 종료기간 정보
     * @return MemberCareerResponse
     */
    @PostMapping
    public ResponseEntity<MemberCareerResponse> saveCareer(@Valid @RequestBody MemberCareerSaveRequest request) {
        MemberCareerResponse response = careerService.saveCareer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 경력정보 수정
     * @param request 경력정보 PK 번호, 근무처, 경력 시작 및 종료기간 정보
     * @return MemberCareerResponse
     */
    @PatchMapping
    public ResponseEntity<MemberCareerResponse> updateCareer(@Valid @RequestBody MemberCareerUpdateRequest request) {
        MemberCareerResponse response = careerService.updateCareer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 경력정보 삭제
     * @param careerId 경력정보 PK 번호
     * @return MemberCareerResponse
     */
    @DeleteMapping("/{careerId}")
    public ResponseEntity<MemberCareerResponse> removeCareer(@Valid @PathVariable Long careerId) {
        MemberCareerResponse response = careerService.removeCareer(careerId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
