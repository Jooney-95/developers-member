package com.developers.member.career.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberCareerResponse {
    private String code;
    private String msg;
    private CareerIdResponse data;
}
