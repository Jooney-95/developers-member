package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberResumeSaveResponse {
    private String code;
    private String msg;
    private MemberIdResponse data;
}
