package com.developers.member.badge.controller;

import com.developers.member.badge.dto.request.MemberChangeBadgeRequest;
import com.developers.member.badge.dto.request.MemberSaveBadgeRequest;
import com.developers.member.badge.dto.response.MemberChangeBadgeResponse;
import com.developers.member.badge.dto.response.MemberAllBadgeResponse;
import com.developers.member.badge.dto.response.MemberPickBadgeResponse;
import com.developers.member.badge.dto.response.MemberSaveBadgeResponse;
import com.developers.member.badge.service.BadgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member/badge")
public class BadgeController {
    private final BadgeService badgeService;


    /**
     * 내가 획득한 모든 칭호 조회
     *
     * @param memberId 사용자 PK 번호
     * @return MemberGetBadgeResponse
     */
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberAllBadgeResponse> getMyAllBadge(@PathVariable Long memberId) {
        MemberAllBadgeResponse response = badgeService.getMyAllBadge(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 내가 착용한 칭호 조회
     *
     * @param memberId 사용자 PK 번호
     * @return MemberPickBadgeResponse
     */
    @GetMapping("/{memberId}/pick")
    public ResponseEntity<MemberPickBadgeResponse> getMyPickBadge(@PathVariable Long memberId) {
        MemberPickBadgeResponse response = badgeService.getMyPickBadge(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 나의 칭호 변경
     * 획득한 칭호 중 하나를 선택하여 착용 칭호를 변경함.
     * @param request 사용자 PK 번호, 변경할 칭호정보
     * @return MemberChangeBadgeResponse
     */
    @PatchMapping
    public ResponseEntity<MemberChangeBadgeResponse> changeBadge(@Valid @RequestBody MemberChangeBadgeRequest request) {
        MemberChangeBadgeResponse response = badgeService.changeBadge(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 획득한 칭호를 칭호목록에 추가
     * 특정 조건을 만족하여 획득한 칭호를 칭호목록에 추가함.
     * @param request 사용자 PK 번호, 달성한 칭호정보
     * @return MemberSaveBadgeResponse
     */
    @PostMapping
    public ResponseEntity<MemberSaveBadgeResponse> archieveBadge(@Valid @RequestBody MemberSaveBadgeRequest request) {
        MemberSaveBadgeResponse response = badgeService.saveBadgeList(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
