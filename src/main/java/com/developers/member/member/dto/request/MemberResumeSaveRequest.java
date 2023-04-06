package com.developers.member.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResumeSaveRequest {
    private Long memberId;
    private String introduce;
    private String positions;
    private String skills;
}
