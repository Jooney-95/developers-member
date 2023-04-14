package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.request.NicknameUpdateRequest;
import com.developers.member.member.dto.request.PasswordChangeRequest;
import com.developers.member.member.dto.request.ProfileImageUpdateRequest;
import com.developers.member.member.dto.response.*;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @ExtendWith(MockitoExtension.class): Mockito를 사용한 테스트를 위한 어노테이션으로 Mockito 관련 기능을 사용하도록 설정
 * @Mock: 모키토(Mockito)를 사용하여 Mock 객체를 생성하는 어노테이션으로 MemberRepository 인터페이스를 구현한 Mock 객체를 생성
 * @InjectMocks: MemberServiceImpl 클래스에 @Mock으로 생성된 MemberRepository Mock 객체를 주입
 *
 */
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private MemberServiceImpl memberService;

    @DisplayName("사용자 회원가입")
    @Test
    public void register() {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("test1@kakao.com")
                .nickname("test1")
                .password(passwordEncoder.encode("kakao123"))
                .profileImageUrl("/root/1")
                .build();
        Member member = request.toEntity();
        when(memberRepository.save(any())).thenReturn(member);

        // when
        MemberRegisterResponse response = memberService.register(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("회원가입이 정상적으로 처리되었습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithPointResponse.class);
        assertThat(response.getData().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(response.getData().getPoint()).isEqualTo(100L);
    }

    @DisplayName("작성자 닉네임 정보 조회")
    @Test
    void getWriterInfo() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("lango@kakao.com")
                .password("kakao123")
                .nickname("lango")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberInfoResponse response = memberService.getWriterInfo(memberId);

        // then
        assertThat(response.getMemberName()).isEqualTo("lango");
    }

    @DisplayName("멘토 닉네임 정보 조회")
    @Test
    void getMentorInfo() {
        // given
        Long memberId = 2L;
        Member member = Member.builder()
                .email("mentor@kakao.com")
                .password("kakao123")
                .nickname("mentor")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(true)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberInfoResponse response = memberService.getWriterInfo(memberId);

        // then
        assertThat(response.getMemberName()).isEqualTo("mentor");
    }

    @DisplayName("나의 개인정보 조회 - Member와 Point 모두 조회")
    @Test
    public void getProfile() {
        // given
        Member member = Member.builder()
                .email("profile@kakao.com")
                .password("kakao123")
                .nickname("myProfile")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        ProfileGetResponse response = memberService.getProfile(member.getMemberId());

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 나의 정보를 조회하였습니다.");
        assertThat(response.getData()).isInstanceOf(ProfileResponse.class);
        assertThat(response.getData().getEmail()).isEqualTo(member.getEmail());
        assertThat(response.getData().getNickname()).isEqualTo(member.getNickname());
        assertThat(response.getData().getPoint()).isEqualTo(member.getPoint().getPoint());
    }

    @DisplayName("사용자 닉네임 변경")
    @Test
    public void updateNickname() {
        // given
        NicknameUpdateRequest request = NicknameUpdateRequest.builder()
                .memberId(1L)
                .nickname("test002")
                .build();
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        NicknameUpdateResponse response = memberService.updateNickname(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 닉네임을 변경하였습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithNicknameResponse.class);
        assertThat(response.getData().getNickname()).isEqualTo(request.getNickname());
    }
    @DisplayName("사용자 프로필 이미지 변경")
    @Test
    public void updateProfileImage() {
        // given
        ProfileImageUpdateRequest request = ProfileImageUpdateRequest.builder()
                .memberId(1L)
                .imagePath("/root/test002/default/1")
                .build();
        Member member = Member.builder()
                .email("test002@kakao.com")
                .password("kakao123")
                .nickname("test002")
                .type(Type.LOCAL)
                .role(Role.USER)
                .profileImageUrl("/root/default/1")
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        ProfileImageUpdateResponse response = memberService.updateProfileImg(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 프로필 이미지를 변경하였습니다.");
        assertThat(response.getData()).isInstanceOf(ProfileImgPathResponse.class);
        assertThat(response.getData().getPath()).isEqualTo(request.getImagePath());
    }

    @DisplayName("사용자 비밀번호 변경")
    @Test
    public void changePassword() {
        // given
        PasswordChangeRequest request = PasswordChangeRequest.builder()
                .memberId(1L)
                .password("kakaocloudschool123!")
                .build();
        Member member = Member.builder()
                .email("test003@kakao.com")
                .password("kakao123")
                .nickname("test003")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        PasswordChangeResponse response = memberService.changePassword(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 비밀번호가 변경되었습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdResponse.class);
    }
}

