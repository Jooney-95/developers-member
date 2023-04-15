package com.developers.member.member.repository;

import com.developers.member.config.JpaConfig;
import com.developers.member.config.SecurityConfig;
import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @DataJpaTest: JPA 테스트를 위한 어노테이션으로 JPA 관련 설정만 로드하여 테스트를 수행
 * @AutoConfigureTestDatabase: 테스트용 데이터베이스를 자동으로 구성해주는 어노테이션으로 replace 속성을 NONE으로 설정하여 실제 데이터베이스를 사용하여 테스트를 수행
 * @ActiveProfiles("local"): 테스트에 application-member-local.yml 설정파일을 사용하도록 설정
 * @Import(JpaConfig.class): 테스트 시 JpaConfig 클래스를 사용하도록 설정
 */

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("member-prod")
@WithMockUser(roles = "USER")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

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
                .isMentor(false)
                .address("서울특별시 강남구")
                .introduce("안녕하세요 저는 ...")
                .position("Software Engineer,Backend Engineer")
                .skills("Spring,Java,MySQL")
                .point(100L)
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
                    .email("test+" + i + "+@kakao.com")
                    .password("kakao123")
                    .nickname("kakao@" + i)
                    .type(Type.LOCAL)
                    .role(Role.USER)
                    .isMentor(false)
                    .point(100L)
                    .build();
            memberRepository.save(member);
        });
        // when
        List<Member> allMembers = memberRepository.findAll();
        for (Member member : allMembers) {
            System.out.println(">>> member saved #" + member.getMemberId() + " member:" + member.toString());
        }
        // then
        for (Member member : allMembers) {
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
                .point(100L)
                .build();
        Member member2 = Member.builder()
                .email("test2@kakao.com")
                .password("kakao123")
                .nickname("kakao@2")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        // when
        Member findMember1 = memberRepository.findById(member1.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Wrong MemberId: <" + member1.getMemberId() + ">"));
        Member findMember2 = memberRepository.findById(member2.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Wrong MemberId: <" + member2.getMemberId() + ">"));
        // then
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(findMember1.getNickname()).isEqualTo("kakao@1");
        assertThat(findMember2.getNickname()).isEqualTo("kakao@2");
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
                .point(100L)
                .build();
        Member mentor = Member.builder()
                .email("mentor@kakao.com")
                .password("kakao123")
                .nickname("mentor@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(true)
                .point(100L)
                .build();
        memberRepository.save(member);
        memberRepository.save(mentor);
        // when
        Member member1 = memberRepository.findById(member.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Wrong MemberId:<" + member.getMemberId() + ">"));
        Member mentor1 = memberRepository.findByIsMentorIsAndMemberId(mentor.isMentor(), mentor.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("Wrong MemberId:<" + mentor.getMemberId() + ">"));
        // then
        assertThat(memberRepository.count()).isEqualTo(2);
        assertThat(member1.isMentor()).isFalse();
        assertThat(mentor1.isMentor()).isTrue();
    }

    @DisplayName("나의 개인정보 조회")
    @Test
    public void getProfile() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("member@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        memberRepository.save(member);
        // when
        List<Member> memberList = memberRepository.findAll();
        Member result = memberList.get(0);

        // then
        Assertions.assertEquals(result, member);
    }

    @DisplayName("사용자 닉네임 변경")
    @Test
    public void updateMemberNickname() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("member@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        Member saveMember = memberRepository.save(member);
        saveMember.updateNickname("member@2");

        // when
        List<Member> memberList = memberRepository.findAll();
        Member result = memberList.get(0);

        // then
        Assertions.assertEquals(result.getNickname(), "member@2");
    }

    @DisplayName("프로필 이미지 변경")
    @Test
    public void updateProfileImage() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("member@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .profileImageUrl("/root/1")
                .point(100L)
                .build();
        Member saveMember = memberRepository.save(member);

        // when
        saveMember.updateProfileImageUrl("/root/test/1");
        Member result = memberRepository.save(member);

        // then
        Assertions.assertEquals(result.getProfileImageUrl(), "/root/test/1");
    }

    @DisplayName("사용자 비밀번호 변경")
    @Test
    public void updatePassword() {
        // given
        Member member = Member.builder()
                .email("test001@kakao.com")
                .password("kakao123")
                .nickname("member@1")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        Member saveMember = memberRepository.save(member);

        // when
        saveMember.changePassword("kakaocloudschool123");
        Member result = memberRepository.save(member);

        // then
        Assertions.assertEquals(result.getPassword(), "kakaocloudschool123");
    }

    @DisplayName("이력정보 등록 및 수정 - 간단소개글, 직무, 주요기술")
    @Test
    public void updateMemberResume() {
        // given
        Member member = Member.builder()
                .email("resume001@kakao.com")
                .password("kakao123")
                .nickname("resume001")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .point(100L)
                .build();
        Member saveMember = memberRepository.save(member);

        // when
        saveMember.updateIntroduce("안녕하세요 저는 백엔드 개발자 xxx 입니다.");
        saveMember.updatePosition("웹 개발자,소프트웨어 엔지니어,자바 개발자");
        saveMember.updateSkills("Java,Spring,MySQL");
        Member result = memberRepository.save(member);

        // then
        assertThat(result.getIntroduce()).isEqualTo(saveMember.getIntroduce());
        assertThat(result.getPosition()).isEqualTo(saveMember.getPosition());
        assertThat(result.getSkills()).isEqualTo(saveMember.getSkills());
    }
}
