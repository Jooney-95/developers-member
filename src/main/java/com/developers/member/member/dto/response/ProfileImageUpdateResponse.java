package com.developers.member.member.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileImageUpdateResponse {
    private String code;
    private String msg;
    private ProfileImgPathResponse data;
}
