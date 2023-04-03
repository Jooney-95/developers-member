package com.developers.member.point.service;

import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.point.dto.request.MemberPointRequest;
import com.developers.member.point.dto.response.MemberPointResponse;
import com.developers.member.point.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class PointServiceImpl implements PointService {
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    @Transactional
    @Override
    public MemberPointResponse increasePoint(MemberPointRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                member.get().increasePoint(10L);
                MemberIdWithPointResponse res = MemberIdWithPointResponse.builder()
                        .memberId(member.get().getMemberId())
                        .point(member.get().getPoint().getPoint())
                        .build();
                return MemberPointResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 점수를 적립하였습니다.")
                        .data(res)
                        .build();
            } else {
                return MemberPointResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberPointResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("점수를 적립하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public MemberPointResponse decreasePoint(MemberPointRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                if(member.get().getPoint().getPoint() < 30L) {
                    return MemberPointResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .msg("보유한 포인트가 부족하여 요청을 처리할 수 없습니다.")
                            .data(null)
                            .build();
                }
                member.get().decreasePoint(30L);
                MemberIdWithPointResponse res = MemberIdWithPointResponse.builder()
                        .memberId(member.get().getMemberId())
                        .point(member.get().getPoint().getPoint())
                        .build();
                return MemberPointResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 점수를 차감하였습니다.")
                        .data(res)
                        .build();
            } else {
                return MemberPointResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            return MemberPointResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("점수를 차감하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }
}
