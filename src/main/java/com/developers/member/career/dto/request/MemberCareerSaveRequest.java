package com.developers.member.career.dto.request;

import com.developers.member.career.dto.response.MemberOfCareer;
import com.developers.member.career.entity.Career;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class MemberCareerSaveRequest {
    private Long memberId;
    private String company;
    private LocalDate careerStart;
    private LocalDate careerEnd;
}
