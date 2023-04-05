package com.developers.member.career.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class CareerInfo {
    private String introduce;
    private String positions;
    private String skills;
    private List<MemberOfCareer> careerList;
}