package com.developers.member.service;

import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.point.dto.request.MemberPointRequest;
import com.developers.member.point.dto.response.MemberPointResponse;
import com.developers.member.point.repository.PointRepository;
import com.developers.member.point.service.PointServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PointRepository pointRepository;
    @InjectMocks
    private PointServiceImpl pointService;

    @DisplayName("문제 정답 풀이를 통한 포인트 적립")
    @Test
    public void increasePoint() {
        // given
        MemberPointRequest request = MemberPointRequest.builder()
                .memberId(1L)
                .build();
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .profileImageUrl("/root/1")
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberPointResponse response = pointService.increasePoint(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 점수를 적립하였습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithPointResponse.class);
        assertThat(response.getData().getPoint()).isEqualTo(110L);
    }

    @DisplayName("멘토링룸 일정 신청을 통한 포인트 차감")
    @Test
    public void decreasePoint() {
        // given
        MemberPointRequest request = MemberPointRequest.builder()
                .memberId(1L)
                .build();
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .profileImageUrl("/root/1")
                .isMentor(false)
                .point(100L)
                .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberPointResponse response = pointService.decreasePoint(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 점수를 차감하였습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithPointResponse.class);
        assertThat(response.getData().getPoint()).isEqualTo(70L);
    }
}
