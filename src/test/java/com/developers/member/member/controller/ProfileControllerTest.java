package com.developers.member.member.controller;

import com.developers.member.member.dto.request.AddressUpdateRequest;
import com.developers.member.member.dto.request.NicknameUpdateRequest;
import com.developers.member.member.dto.request.PasswordChangeRequest;
import com.developers.member.member.dto.request.ProfileImageUpdateRequest;
import com.developers.member.member.dto.response.*;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(ProfileController.class)
@WithMockUser(roles = "USER")
public class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    @DisplayName("개인정보 조회")
    @Test
    public void 사용자_개인정보_조회() throws Exception {
        // given
        Long memberId = 1L;
        ProfileResponse profile = ProfileResponse.builder()
                .email("lango@kakao.com")
                .nickname("lango")
                .profileImageUrl("/root/1")
                .isMentor(false)
                .address("서울")
                .introduce("저는 개발자입니다.")
                .position("백엔드,데브옵스")
                .skills("Java,Spring Boot")
                .point(100L)
                .build();
        ProfileGetResponse response = ProfileGetResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 나의 정보를 조회하였습니다.")
                .data(profile)
                .build();
        given(memberService.getProfile(any())).willReturn(response);

        // when
        mockMvc.perform(get("/api/member/" + memberId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/profile/get-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 닉네임 변경")
    @Test
    public void 사용자_닉네임_변경() throws Exception {
        // given
        NicknameUpdateRequest request = NicknameUpdateRequest.builder()
                .memberId(1L)
                .nickname("langoustine")
                .build();
        MemberIdWithNicknameResponse memberIdWithNickname = MemberIdWithNicknameResponse.builder()
                .memberId(1L)
                .nickname("langoustine")
                .build();
        NicknameUpdateResponse response = NicknameUpdateResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 닉네임을 변경하였습니다.")
                .data(memberIdWithNickname)
                .build();
//        given(memberService.register(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/nickname")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/profile/update-nickname",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 프로필 이미지 변경")
    @Test
    public void 사용자_프로필_이미지_변경() throws Exception {
        // given
        ProfileImageUpdateRequest request = ProfileImageUpdateRequest.builder()
                .memberId(1L)
                .imagePath("/root/update/1")
                .build();
        ProfileImgPathResponse imagePath = ProfileImgPathResponse.builder()
                .path("/root/update/1")
                .build();
        ProfileImageUpdateResponse response = ProfileImageUpdateResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 프로필 이미지를 변경하였습니다.")
                .data(imagePath)
                .build();
        given(memberService.updateProfileImg(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/profileimg")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/profile/update-profileimg",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).updateProfileImg(request);
    }

    @DisplayName("사용자 개인 비밀번호 변경")
    @Test
    public void 사용자_비밀번호_변경() throws Exception {
        // given
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .memberId(1L)
                .password("kakao123")
                .build();
        MemberIdResponse memberId = MemberIdResponse.builder()
                .memberId(1L)
                .build();
        PasswordChangeResponse response = PasswordChangeResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 비밀번호가 변경되었습니다.")
                .data(memberId)
                .build();
        given(memberService.changePassword(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/password")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/profile/change-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).updateProfileImg(request);
    }

    @DisplayName("사용자 거주지 변경")
    @Test
    public void 사용자_거주지_변경() throws Exception {
        // given
        AddressUpdateRequest request = AddressUpdateRequest.builder()
                .memberId(1L)
                .address("부산")
                .build();
        MemberIdResponse memberId = MemberIdResponse.builder()
                .memberId(1L)
                .build();
        AddressUpdateResponse response = AddressUpdateResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 거주지 정보를 변경했습니다.")
                .data(memberId)
                .build();
        given(memberService.updateAddress(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/address")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/profile/update-address",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).updateProfileImg(request);
    }
}
