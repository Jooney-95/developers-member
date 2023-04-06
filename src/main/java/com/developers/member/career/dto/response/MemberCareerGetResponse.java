package com.developers.member.career.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberCareerGetResponse {
    private String code;
    private String msg;
    private CareerInfo data;
}

