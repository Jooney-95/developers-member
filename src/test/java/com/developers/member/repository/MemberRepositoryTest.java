package com.developers.member.repository;

import com.developers.member.config.JpaConfig;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import jakarta.persistence.EntityListeners;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @DataJpaTest: JPA 테스트를 위한 어노테이션으로 JPA 관련 설정만 로드하여 테스트를 수행
 * @AutoConfigureTestDatabase: 테스트용 데이터베이스를 자동으로 구성해주는 어노테이션으로 replace 속성을 NONE으로 설정하여 실제 데이터베이스를 사용하여 테스트를 수행
 * @ActiveProfiles("local"): 테스트에 application-local.yml 설정파일을 사용하도록 설정
 * @Import(JpaConfig.class): 테스트 시 JpaConfig 클래스를 사용하도록 설정
 *
 */

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("prod")
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    @DisplayName("한명의 회원 데이터 저장")
    @Test
    public void save() {
        // given
        Member member = Member.builder()
                .email("lango@kakao.com")
                .password("kakao123")
                .nickname("lango")
                .type(Type.LOCAL)
                .role(Role.USER)
                .profileImageUrl("/root/1")
                .isMentor(true)
                .address("서울특별시 강남구")
                .introduce("안녕하세요 저는 ...")
                .build();
        memberRepository.save(member);

        // when
        List<Member> list = memberRepository.findAll();

        // then
        Member result = list.get(0);
        Assertions.assertEquals("lango@kakao.com", result.getEmail());
        Assertions.assertEquals("안녕하세요 저는 ...", result.getIntroduce());
        Assertions.assertEquals("lango", result.getNickname());
    }

    @DisplayName("50명의 회원 데이터 저장")
    @Test
    public void bulkSave() {
        // given
        IntStream.rangeClosed(1, 50).forEach(i -> {
            Member member = Member.builder()
                    .email("test+"+i+"+@kakao.com")
                    .password("kakao123")
                    .nickname("kakao@"+i)
                    .type(Type.LOCAL)
                    .role(Role.USER)
                    .isMentor(false)
                    .profileImageUrl("/root/"+i)
                    .build();
            memberRepository.save(member);
        });
        // when
        List<Member> allMembers = memberRepository.findAll();
        for (Member member: allMembers) {
            System.out.println(">>> member saved #" + member.getMemberId() + " member:" + member.toString());
        }
        // then
        for (Member member: allMembers) {
            assertThat(member.getMemberId()).isNotNull();
        }
    }


    @DisplayName("두 명의 회원 조회")
    @Test
    public void findById() {
        // given
        Member member1 = Member.builder()
                .email("test1@kakao.com")
                .password("kakao123")
                .nickname("kakao@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .profileImageUrl("/root/1")
                .build();
        Member member2 = Member.builder()
                .email("test2@kakao.com")
                .password("kakao123")
                .nickname("kakao@2")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .profileImageUrl("/root/2")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        // when
        Member findMember1 = memberRepository.findById(member1.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + member1.getMemberId() + ">"));
        Member findMember2 = memberRepository.findById(member2.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + member2.getMemberId() + ">"));
        // then
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(findMember1.getNickname()).isEqualTo("kakao@1");
        assertThat(findMember1.getProfileImageUrl()).isEqualTo("/root/1");
        assertThat(findMember2.getNickname()).isEqualTo("kakao@2");
        assertThat(findMember2.getProfileImageUrl()).isEqualTo("/root/2");
    }

    @DisplayName("멘토 회원 조회")
    @Test
    public void memberIsMentor() {
        // given
        Member member = Member.builder()
                .email("menber111@kakao.com")
                .password("kakao123")
                .nickname("member@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .profileImageUrl("/root/1")
                .build();
        Member mentor = Member.builder()
                .email("mentor@kakao.com")
                .password("kakao123")
                .nickname("mentor@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(true)
                .profileImageUrl("/root/2")
                .build();
        memberRepository.save(member);
        memberRepository.save(mentor);
        // when
        Member member1 = memberRepository.findById(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + member.getMemberId() + ">"));
        Member mentor1 = memberRepository.findByIsMentorIsAndMemberId(mentor.isMentor(), mentor.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + mentor.getMemberId() + ">"));
        // then
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(member1.isMentor()).isFalse();
        assertThat(mentor1.isMentor()).isTrue();
    }
}
