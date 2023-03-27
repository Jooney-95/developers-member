package com.developers.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberInfoResponse;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.member.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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

    @InjectMocks
    private MemberServiceImpl memberService;

    @DisplayName("사용자 회원가입")
    @Test
    public void register() {
        // given
        MemberRegisterRequest request = MemberRegisterRequest.builder()
                .email("test1@kakao.com")
                .nickName("test1")
                .password("kakao123")
                .profileImageUrl("/root/1")
                .build();
        Member member = request.toEntity();
        when(memberRepository.save(any())).thenReturn(member);

        // when
        MemberRegisterResponse response = memberService.register(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.name());
        assertThat(response.getMsg()).isEqualTo("회원가입이 정상적으로 처리되었습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdResponse.class);
        assertThat(response.getData().getMemberId()).isEqualTo(member.getMemberId());
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
                .profileImageUrl("/root/1")
                .isMentor(false)
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
                .profileImageUrl("/root/1")
                .isMentor(true)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberInfoResponse response = memberService.getWriterInfo(memberId);

        // then
        assertThat(response.getMemberName()).isEqualTo("mentor");
    }
}

