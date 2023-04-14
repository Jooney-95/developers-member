package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressUpdateResponse {
    private String code;
    private String msg;
    private MemberIdResponse data;
}
