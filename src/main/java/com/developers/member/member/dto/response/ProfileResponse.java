package com.developers.member.member.dto.response;

import com.developers.member.point.entity.Point;
import lombok.Builder;
import lombok.Getter;

/**
 * 마이페이지에서 개인정보 조회시 전달할 Member 데이터를 가지는 응답 DTO
 */
@Builder
@Getter
public class ProfileResponse {
    private String email;
    private String nickname;
    private String profileImageUrl;
    private boolean isMentor;
    private String address;
    private String introduce;
    private String position;
    private String skills;
    private Long point;
}
