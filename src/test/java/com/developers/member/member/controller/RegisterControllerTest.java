package com.developers.member.member.controller;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.dto.response.MemberRemoveResponse;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(RegisterController.class)
@WithMockUser(roles = "USER")
public class RegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @DisplayName("사용자 회원가입")
    @Test
    public void 사용자_회원가입() throws Exception {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("lango@kakao.com")
                .password("kakao123")
                .nickname("lango")
                .profileImageUrl("/root/1")
                .address("서울")
                .position("백엔드,데브옵스")
                .skills("Java,Spring")
                .build();
        MemberIdWithPointResponse data = MemberIdWithPointResponse.builder()
                .memberId(1L)
                .point(100L)
                .build();
        MemberRegisterResponse response = MemberRegisterResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("회원가입이 정상적으로 처리되었습니다.")
                .data(data)
                .build();
        given(memberService.register(any())).willReturn(response);

        // when
        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/register/member-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 회원탈퇴")
    @Test
    public void 사용자_회원탈퇴() throws Exception {
        // given
        Long memberId = 1L;
        MemberIdResponse memberIdresponse = MemberIdResponse.builder().memberId(memberId).build();
        MemberRemoveResponse response = MemberRemoveResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 사용자 회원 탈퇴를 처리했습니다.")
                .data(memberIdresponse)
                .build();
        given(memberService.removeMember(any())).willReturn(response);

        // when
        mockMvc.perform(delete("/api/auth/" + memberId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/register/member-unregister",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }
}
