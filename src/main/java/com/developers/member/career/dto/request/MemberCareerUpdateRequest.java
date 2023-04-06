package com.developers.member.career.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MemberCareerUpdateRequest {
    private Long careerId;
    private String company;
    private LocalDate careerStart;
    private LocalDate careerEnd;
}
