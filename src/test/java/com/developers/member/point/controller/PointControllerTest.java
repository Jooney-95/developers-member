package com.developers.member.point.controller;

import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import com.developers.member.point.dto.request.MemberPointRequest;
import com.developers.member.point.dto.response.MemberPointResponse;
import com.developers.member.point.service.PointService;
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
@WebMvcTest(PointController.class)
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PointService pointService;

    @DisplayName("사용자 포인트 적립(+10점)")
    @Test
    public void 사용자_포인트_적립() throws Exception {
        // given
        MemberPointRequest request = MemberPointRequest.builder()
                .memberId(1L)
                .build();
        MemberIdWithPointResponse memberIdWithPointResponse = MemberIdWithPointResponse.builder()
                .memberId(1L)
                .point(100L)
                .build();
        MemberPointResponse response = MemberPointResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 점수를 적립하였습니다.")
                .data(memberIdWithPointResponse)
                .build();
        given(pointService.increasePoint(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/point/increase")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/point/increase",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 포인트 차감(-30점)")
    @Test
    public void 사용자_포인트_차감() throws Exception {
        // given
        MemberPointRequest request = MemberPointRequest.builder()
                .memberId(1L)
                .build();
        MemberIdWithPointResponse memberIdWithPointResponse = MemberIdWithPointResponse.builder()
                .memberId(1L)
                .point(100L)
                .build();
        MemberPointResponse response = MemberPointResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 점수를 차감하였습니다.")
                .data(memberIdWithPointResponse)
                .build();
        given(pointService.decreasePoint(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/point/decrease")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/point/decrease",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }
}
