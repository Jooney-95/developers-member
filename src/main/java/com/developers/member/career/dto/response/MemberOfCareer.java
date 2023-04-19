package com.developers.member.career.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class MemberOfCareer {
    private Long careerId;
    private String company;
    private LocalDate careerStart;
    private LocalDate careerEnd;
}
