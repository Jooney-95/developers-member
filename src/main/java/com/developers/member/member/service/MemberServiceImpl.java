package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 *
 * register: 회원가입
 */
@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public MemberRegisterResponse register(MemberRegisterRequest request) {
        Member member = request.toEntity();
        Long saveMemberId = memberRepository.save(member).getMemberId();
        MemberIdResponse memberIdResponse = MemberIdResponse.builder().memberId(saveMemberId).build();
        return MemberRegisterResponse.builder()
                .code(HttpStatus.OK.name())
                .msg("회원가입이 정상적으로 처리되었습니다.")
                .data(memberIdResponse)
                .build();
    }
}
