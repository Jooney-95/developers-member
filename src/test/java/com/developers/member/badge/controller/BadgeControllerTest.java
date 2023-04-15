package com.developers.member.badge.controller;

import com.developers.member.badge.dto.request.MemberChangeBadgeRequest;
import com.developers.member.badge.dto.request.MemberSaveBadgeRequest;
import com.developers.member.badge.dto.response.*;
import com.developers.member.badge.entity.Badges;
import com.developers.member.badge.service.BadgeService;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberIdWithPointResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(BadgeController.class)
@WithMockUser(roles = "USER")
public class BadgeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BadgeService bBadgeService;

    @DisplayName("사용자의 모든 획득 칭호 조회")
    @Test
    public void 사용자_획득_칭호목록_조회() throws Exception {
        // given
        Long memberId = 1L;
        MemberIdWithPointResponse memberIdWithPointResponse = MemberIdWithPointResponse.builder()
                .memberId(1L)
                .point(100L)
                .build();
        List<MyBadgeList> myBadgeList = new ArrayList<>();
        MyBadgeList badge = MyBadgeList.builder()
                .badge(String.valueOf(Badges.BEGINNER))
                .build();
        myBadgeList.add(badge);
        MemberIdWithAllBadgeResponse memberIdWithAllBadgeResponse = MemberIdWithAllBadgeResponse.builder()
                .memberId(1L)
                .myBadgeList(myBadgeList)
                .build();
        MemberAllBadgeResponse response = MemberAllBadgeResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 사용자의 획득한 칭호 목록을 조회했습니다.")
                .data(memberIdWithAllBadgeResponse)
                .build();
        given(bBadgeService.getMyAllBadge(any())).willReturn(response);

        // when
        mockMvc.perform(get("/api/member/badge/" + memberId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/badge/get-all-badge",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자의 착용칭호 조회")
    @Test
    public void 사용자_착용칭호_조회() throws Exception {
        // given
        Long memberId = 1L;
        MemberIdWithPickBadgeResponse memberIdWithAllBadgeResponse = MemberIdWithPickBadgeResponse.builder()
                .memberId(1L)
                .myBadge(Badges.BEGINNER.toString())
                .build();
        MemberPickBadgeResponse response = MemberPickBadgeResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 사용자의 착용한 칭호 정보를 조회했습니다.")
                .data(memberIdWithAllBadgeResponse)
                .build();
        given(bBadgeService.getMyPickBadge(any())).willReturn(response);

        // when
        mockMvc.perform(get("/api/member/badge/" + memberId + "/pick")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/badge/get-pick-badge",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 착용칭호 변경")
    @Test
    public void 사용자_착용칭호_변경() throws Exception {
        // given
        MemberChangeBadgeRequest request = MemberChangeBadgeRequest.builder()
                .memberId(1L)
                .badgeName(String.valueOf(Badges.BEGINNER))
                .build();
        MemberIdWithPickBadgeResponse memberIdWithAllBadgeResponse = MemberIdWithPickBadgeResponse.builder()
                .memberId(1L)
                .myBadge(Badges.BEGINNER.toString())
                .build();
        MemberChangeBadgeResponse response = MemberChangeBadgeResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 사용자의 착용한 칭호 정보를 조회했습니다.")
                .data(memberIdWithAllBadgeResponse)
                .build();
        given(bBadgeService.changeBadge(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/badge")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/badge/change-badge",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 칭호 획득")
    @Test
    public void 사용자_칭호_획득() throws Exception {
        // given
        MemberSaveBadgeRequest request = MemberSaveBadgeRequest.builder()
                .memberId(1L)
                .archieveBadge(Badges.BEGINNER)
                .build();
        MemberIdResponse memberId = MemberIdResponse.builder()
                .memberId(1L)
                .build();
        MemberSaveBadgeResponse response = MemberSaveBadgeResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 칭호를 변경했습니다.")
                .data(memberId)
                .build();
        given(bBadgeService.saveBadgeList(any())).willReturn(response);

        // when
        mockMvc.perform(post("/api/member/badge")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/badge/save-badge",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }
}
