package com.developers.member.attach.dto.response;

import com.developers.member.member.dto.response.MemberIdResponse;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberProfileImgUpdateResponse {
    private String code;
    private String msg;

    private MemberIdResponse data;
}
