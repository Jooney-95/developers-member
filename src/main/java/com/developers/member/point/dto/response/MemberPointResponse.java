package com.developers.member.point.dto.response;

import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberPointResponse {

    private String code;
    private String msg;
    private MemberIdWithPointResponse data;
}
