package com.developers.member.repository;

import com.developers.member.member.entity.Member;
import com.developers.member.member.entity.Role;
import com.developers.member.member.entity.Type;
import com.developers.member.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;
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
}
