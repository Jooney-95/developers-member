package com.developers.member.badge.service;

import com.developers.member.badge.dto.request.MemberChangeBadgeRequest;
import com.developers.member.badge.dto.request.MemberSaveBadgeRequest;
import com.developers.member.badge.dto.response.*;
import com.developers.member.badge.entity.Badge;
import com.developers.member.badge.entity.Badges;
import com.developers.member.badge.entity.MyBadge;
import com.developers.member.badge.repository.BadgeRepository;
import com.developers.member.badge.repository.MyBadgeRepository;
import com.developers.member.member.dto.response.MemberIdResponse;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BadgeServiceTest {
    @Mock
    MyBadgeRepository myBadgeRepository;

    @Mock
    BadgeRepository badgeRepository;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    BadgeServiceImpl badgeService;

    @DisplayName("사용자가 획득한 모든 칭호 조회")
    @Test
    public void getMyAllBadge() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        List<Badge> badges = new ArrayList<>();
        Badge badge1 = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_SOLVER)
                .build();
        Badge badge2 = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_MENTOR)
                .build();
        badges.add(badge1);
        badges.add(badge2);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(badgeRepository.findByMember(member)).thenReturn(badges);

        // when
        MemberAllBadgeResponse response = badgeService.getMyAllBadge(memberId);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 사용자의 획득한 칭호 목록을 조회했습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithAllBadgeResponse.class);
        assertThat(response.getData().getMyBadgeList().size()).isEqualTo(2);
    }

    @DisplayName("사용자가 착용한 칭호 조회")
    @Test
    public void getMyPickBadge() {
        // given
        Long memberId = 1L;
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        MyBadge myBadge = MyBadge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER)
                .build();
        member.setMyBadge(myBadge);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MemberPickBadgeResponse response = badgeService.getMyPickBadge(memberId);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 사용자의 착용한 칭호 정보를 조회했습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithPickBadgeResponse.class);
    }

    @DisplayName("사용자 착용 칭호 변경")
    @Test
    public void changeBadge() {
        MemberChangeBadgeRequest request = MemberChangeBadgeRequest.builder()
                .memberId(1L)
                .badgeName(Badges.BEGINNER_SOLVER.toString())
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

        List<Badge> badges = new ArrayList<>();
        Badge badge1 = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_SOLVER)
                .build();
        Badge badge2 = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_MENTOR)
                .build();
        badges.add(badge1);
        badges.add(badge2);

        MyBadge myBadge = MyBadge.builder()
                .member(member)
                .badgeName(Badges.valueOf(request.getBadgeName()))
                .build();
        member.setMyBadge(myBadge);

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(badgeRepository.findByMember(member)).thenReturn(badges);

        // when
        MemberChangeBadgeResponse response = badgeService.changeBadge(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 칭호를 변경했습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdWithPickBadgeResponse.class);
    }

    @DisplayName("사용자 칭호 획득")
    @Test
    public void saveBadge() {
        MemberSaveBadgeRequest request = MemberSaveBadgeRequest.builder()
                .memberId(1L)
                .archieveBadge(Badges.INTERMEDIATE_MENTOR)
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
        Badge saveBadge = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_SOLVER)
                .build();

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        when(badgeRepository.existsByMemberAndBadgeName(member, request.getArchieveBadge())).thenReturn(false);
        when(badgeRepository.save(any())).thenReturn(saveBadge);

        // when
        MemberSaveBadgeResponse response = badgeService.saveBadgeList(request);

        // then
        assertThat(response.getCode()).isEqualTo(HttpStatus.OK.toString());
        assertThat(response.getMsg()).isEqualTo("정상적으로 획득한 칭호를 추가했습니다.");
        assertThat(response.getData()).isInstanceOf(MemberIdResponse.class);
    }
}