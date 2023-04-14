package com.developers.member.career.repository;

import com.developers.member.career.entity.Career;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaConfig.class)
@ActiveProfiles("prod")
public class CareerRepositoryTest {
    @Autowired
    private CareerRepository careerRepository;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자 경력정보 저장")
    @Test
    public void save() {
        // given
        LocalDate currentDate = LocalDate.now();
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
        Career career1 = Career.builder()
                .member(member)
                .company("카카오")
                .start(currentDate)
                .end(currentDate)
                .build();
        Career career2 = Career.builder()
                .member(member)
                .company("네이버")
                .start(currentDate)
                .end(currentDate)
                .build();

        // when
        memberRepository.save(member);
        careerRepository.save(career1);
        careerRepository.save(career2);
        List<Career> memberCareerList = careerRepository.findByMember(member);
        Career result1 = memberCareerList.get(0);
        Career result2 = memberCareerList.get(1);

        // then
        assertThat(result1.getCompany()).isEqualTo("카카오");
        assertThat(result2.getCompany()).isEqualTo("네이버");
    }

    @DisplayName("사용자 경력정보 수정")
    @Transactional
    @Test
    public void update() {
        // given
        LocalDate currentDate = LocalDate.now();
        Member member = Member.builder()
                .email("test002@kakao.com")
                .password("kakao123")
                .nickname("test002")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .address("서울특별시 서초구")
                .introduce("안녕하세요 저는 프론트엔드 개발자입지다.")
                .position("Software Engineer,Frontend Engineer")
                .skills("React,JS,Redux,Recoil")
                .point(100L)
                .build();
        Career career = Career.builder()
                .member(member)
                .company("카카오")
                .start(currentDate)
                .end(currentDate)
                .build();

        // when
        memberRepository.save(member);
        Career saveCareer = careerRepository.save(career);
        saveCareer.updateCompany("카카오페이");
        List<Career> memberCareerList = careerRepository.findByMember(member);
        Career result = memberCareerList.get(0);

        // then
        assertThat(result.getCompany()).isEqualTo("카카오페이");
    }

    @DisplayName("사용자 경력정보 삭제")
    @Test
    public void removeMemberCareer() {
        // given
        LocalDate currentDate = LocalDate.now();
        Member member = Member.builder()
                .email("test002@kakao.com")
                .password("kakao123")
                .nickname("test002")
                .type(Type.LOCAL)
                .role(Role.USER)
                .isMentor(false)
                .address("서울특별시 서초구")
                .introduce("안녕하세요 저는 프론트엔드 개발자입지다.")
                .position("Software Engineer,Frontend Engineer")
                .skills("React,JS,Redux,Recoil")
                .point(100L)
                .build();
        Career career = Career.builder()
                .member(member)
                .company("카카오")
                .start(currentDate)
                .end(currentDate)
                .build();

        // when
        memberRepository.save(member);
        Career saveCareer = careerRepository.save(career);
        careerRepository.deleteById(saveCareer.getCareerId());
        List<Career> result = careerRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(0);
    }
}
