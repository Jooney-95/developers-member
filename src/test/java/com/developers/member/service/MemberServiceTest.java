package com.developers.member.service;

import com.developers.member.member.repository.MemberRepository;
import com.developers.member.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 사용자_회원가입() {
        // given
//            given(memberRepository.findById(1L)).willReturn()
        // when

        // then
    }

}
