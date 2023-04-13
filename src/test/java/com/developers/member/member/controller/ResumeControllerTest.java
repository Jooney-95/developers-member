package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberResumeSaveRequest;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberResumeSaveResponse;
import com.developers.member.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(ResumeController.class)
public class ResumeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @DisplayName("사용자 이력정보 등록 및 수정")
    @Test
    public void 사용자_이력정보_등록_수정() throws Exception {
        // given
        MemberResumeSaveRequest request = MemberResumeSaveRequest.builder()
                .memberId(1L)
                .introduce("안녕하세요 개발자입니다.")
                .positions("백엔드,인프라")
                .skills("Python,Go")
                .build();
        MemberIdResponse memberId = MemberIdResponse.builder()
                .memberId(1L)
                .build();
        MemberResumeSaveResponse response = MemberResumeSaveResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 이력정보를 등록하였습니다.")
                .data(memberId)
                .build();
        given(memberService.saveMemberResume(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/resume")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/resume/save-member-resumeinfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }
}
