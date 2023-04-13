package com.developers.member.member.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AddressUpdateRequest {
    private Long memberId;
    private String address;
}
