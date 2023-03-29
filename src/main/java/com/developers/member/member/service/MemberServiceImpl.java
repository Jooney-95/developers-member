package com.developers.member.member.service;

import com.developers.member.member.dto.request.MemberRegisterRequest;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.dto.response.MemberRegisterResponse;
import com.developers.member.member.dto.response.MemberInfoResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 *
 * register: 회원가입
 * getWriterInfo: 사용자의 닉네임 정보를 전달하는 Open API
 * getMentorInfo: 멘토인 사용자의 닉네임 정보를 전달하는 Open API
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

    @Override
    public MemberInfoResponse getWriterInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(memberId + " 번호는 존재하지 않는 회원입니다."));
        return MemberInfoResponse.builder()
                .memberName(member.getNickname())
                .build();
    }

    @Override
    public MemberInfoResponse getMentorInfo(Long mentorId) {
        Member member = memberRepository.findByIsMentorIsAndMemberId(true, mentorId)
                .orElseThrow(() -> new NoSuchElementException(mentorId + " 번호는 멘토로 등록되지 않은 회원입니다."));
        return MemberInfoResponse.builder()
                .memberName(member.getNickname())
                .build();
    }
}
