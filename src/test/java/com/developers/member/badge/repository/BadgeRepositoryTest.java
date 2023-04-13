package com.developers.member.badge.repository;

import com.developers.member.badge.entity.MyBadge;
import com.developers.member.badge.entity.Badge;
import com.developers.member.badge.entity.Badges;
import com.developers.member.config.JpaConfig;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("prod")
public class BadgeRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MyBadgeRepository myBadgeRepository;
    @Autowired
    private BadgeRepository badgeRepository;

    @DisplayName("착용한 칭호 조회")
    @Transactional
    @Test
    public void getMyBadge() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .address("서울특별시 강남구")
                .introduce("안녕하세요 저는 ...")
                .position("Software Engineer,Backend Engineer")
                .skills("Spring,Java,MySQL")
                .point(100L)
                .build();
        Badge myBadges = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER)
                .build();
        MyBadge myBadge = MyBadge.builder()
                .member(member)
                .badgeName(myBadges.getBadgeName())
                .build();

        memberRepository.save(member);
        badgeRepository.save(myBadges);
        myBadgeRepository.save(myBadge);

        // when
        MyBadge result = myBadgeRepository.findByMember(member);

        // then
        assertThat(result.getMember()).isEqualTo(member);
        assertThat(result.getBadgeName()).isEqualTo(myBadges.getBadgeName());
    }

    @DisplayName("획득 칭호 목록 조회")
    @Test
    public void getMyBadgeList() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("test001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .address("서울특별시 강남구")
                .introduce("안녕하세요 저는 ...")
                .position("Software Engineer,Backend Engineer")
                .skills("Spring,Java,MySQL")
                .point(100L)
                .build();
        Badge beginnerBadge = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER)
                .build();
        Badge beginnerSolver = Badge.builder()
                .member(member)
                .badgeName(Badges.BEGINNER_SOLVER)
                .build();
        MyBadge myBadge = MyBadge.builder()
                .member(member)
                .badgeName(beginnerSolver.getBadgeName())
                .build();
        memberRepository.save(member);
        badgeRepository.save(beginnerBadge);
        badgeRepository.save(beginnerSolver);
        myBadgeRepository.save(myBadge);

        // when
        List<Badge> result = badgeRepository.findByMember(member);

        // then
        assertThat(badgeRepository.count()).isEqualTo(2);
        assertThat(result.get(0).getBadgeName()).isEqualTo(beginnerBadge.getBadgeName());
        assertThat(result.get(1).getBadgeName()).isEqualTo(beginnerSolver.getBadgeName());

    }
}
