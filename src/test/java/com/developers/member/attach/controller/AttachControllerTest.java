package com.developers.member.attach.controller;

import com.developers.member.attach.dto.response.MemberProfileImgUpdateResponse;
import com.developers.member.attach.service.AttachService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@ExtendWith(SpringExtension.class)
@WebMvcTest(AttachController.class)
@WithMockUser(roles = "USER")
public class AttachControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AttachService attachService;

    @DisplayName("사용자 프로필 이미지 등록")
    @Test
    public void uploadProfileImage() throws Exception {
        Long memberId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        MemberProfileImgUpdateResponse response = MemberProfileImgUpdateResponse.builder()
                .code(HttpStatus.OK.toString())
                .msg("정상적으로 프로필 이미지를 등록했습니다.")
                .build();

        given(attachService.uploadProfileImage(memberId, file)).willReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/attach/profile")
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("memberId", memberId.toString()))
                .andDo(print())
                .andDo(document("attach/profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(response)));
    }
}
