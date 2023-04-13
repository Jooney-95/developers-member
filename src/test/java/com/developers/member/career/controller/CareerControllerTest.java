package com.developers.member.career.controller;

import com.developers.member.career.dto.request.MemberCareerSaveRequest;
import com.developers.member.career.dto.request.MemberCareerUpdateRequest;
import com.developers.member.career.dto.response.*;
import com.developers.member.career.service.CareerService;
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

import java.time.LocalDate;
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
@WebMvcTest(CareerController.class)
public class CareerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CareerService careerService;

    @DisplayName("사용자 경력정보 조회")
    @Test
    public void 사용자_경력정보_조회() throws Exception {
        // given
        Long memberId = 1L;
        List<MemberOfCareer> careerList = new ArrayList<>();
        MemberOfCareer memberOfCareer = MemberOfCareer.builder()
                .company("카카오")
                .careerStart(LocalDate.now())
                .careerEnd(LocalDate.now())
                .build();
        careerList.add(memberOfCareer);
        CareerInfo careerInfo = CareerInfo.builder()
                .introduce("안녕하세요 백엔드 개발자입니다.")
                .positions("백엔드,데브옵스")
                .skills("Java,Spring")
                .careerList(careerList)
                .build();
        MemberCareerGetResponse response = MemberCareerGetResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 이력 및 경력정보를 조회하였습니다.")
                .data(careerInfo)
                .build();
        given(careerService.getCareerInfo(any())).willReturn(response);

        // when
        mockMvc.perform(get("/api/member/career/" + memberId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/career/get-member-careerinfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 경력정보 등록")
    @Test
    public void 사용자_경력정보_등록() throws Exception {
        // given
        MemberCareerSaveRequest request = MemberCareerSaveRequest.builder()
                .company("카카오")
                .careerStart(LocalDate.now())
                .careerEnd(LocalDate.now())
                .build();
        CareerIdResponse careerIdResponss = CareerIdResponse.builder()
                .careerId(1L)
                .build();
        MemberCareerResponse response = MemberCareerResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 경력정보가 등록되었습니다.")
                .data(careerIdResponss)
                .build();
        given(careerService.saveCareer(any())).willReturn(response);

        // when
        mockMvc.perform(post("/api/member/career")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/career/save-member-careerinfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 경력정보 수정")
    @Test
    public void 사용자_경력정보_수정() throws Exception {
        // given
        MemberCareerUpdateRequest request = MemberCareerUpdateRequest.builder()
                .careerId(1L)
                .company("마이다스아이티")
                .careerStart(LocalDate.now())
                .careerEnd(LocalDate.now())
                .build();
        CareerIdResponse careerIdResponss = CareerIdResponse.builder()
                .careerId(1L)
                .build();
        MemberCareerResponse response = MemberCareerResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 경력정보가 변경되었습니다.")
                .data(careerIdResponss)
                .build();
        given(careerService.updateCareer(any())).willReturn(response);

        // when
        mockMvc.perform(patch("/api/member/career")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andDo(document("member/career/update-member-careerinfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }

    @DisplayName("사용자 경력정보 삭제")
    @Test
    public void 사용자_경력정보_삭제() throws Exception {
        // given
        Long memberId = 1L;
        CareerIdResponse careerIdResponss = CareerIdResponse.builder()
                .careerId(1L)
                .build();
        MemberCareerResponse response = MemberCareerResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 경력정보를 삭제하였습니다.")
                .data(careerIdResponss)
                .build();
        given(careerService.removeCareer(any())).willReturn(response);

        // when
        mockMvc.perform(delete("/api/member/career/" + memberId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberId))
                )
                .andDo(print())
                .andDo(document("member/career/delete-member-careerinfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(status().isOk());

        // then
//        verify(memberService, times(1)).register(request);
    }
}
