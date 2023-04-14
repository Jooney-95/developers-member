package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class UpdateMemberRefreshResponse {
    private String code;
    private Long memberId;
}
