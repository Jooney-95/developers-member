package com.developers.member.point.service;

import com.developers.member.member.dto.response.MemberIdWithPointResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
import com.developers.member.point.dto.request.MemberPointRequest;
import com.developers.member.point.dto.response.GetPointRankingResponse;
import com.developers.member.point.dto.response.MemberPointResponse;
import com.developers.member.point.dto.response.PointRanking;
import com.developers.member.point.dto.response.PointWithNickname;
import com.developers.member.point.entity.Point;
import com.developers.member.point.repository.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
                log.info("[PointServiceImpl] 포인트적립: 10점 적립 성공, 사용자 번호: {}, 점수: {}", request.getMemberId(), member.get().getPoint());
                return MemberPointResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 점수를 적립하였습니다.")
                        .data(res)
                        .build();
            } else {
                log.info("[PointServiceImpl] 포인트적립: 존재하지 않는 사용자입니다.");
                return MemberPointResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[PointServiceImpl] 포인트적립: 점수를 적립하던 중 문제가 발생하였습니다.");
            e.printStackTrace();
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
                    log.info("[PointServiceImpl] 포인트차감: 보유한 포인트 부족으로 포인트를 차감할 수 없습니다. 사용자 번호: {}, 점수: {}", request.getMemberId(), member.get().getPoint());
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
                log.info("[PointServiceImpl] 포인트차감: 30점 차감 성공, 사용자 번호: {}, 점수: {}", request.getMemberId(), member.get().getPoint());
                return MemberPointResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 점수를 차감하였습니다.")
                        .data(res)
                        .build();
            } else {
                log.info("[PointServiceImpl] 포인트차감: 존재하지 않는 사용자입니다.");
                return MemberPointResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[PointServiceImpl] 포인트차감: 점수를 차감하던 중 문제가 발생하였습니다.");
            e.printStackTrace();
            return MemberPointResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("점수를 차감하던 중 문제가 발생하였습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public GetPointRankingResponse getPointRanking() {
        log.info("[PointServiceImpl] 포인트랭킹조회: 사용자 포인트 랭킹을 조회합니다.");
        try {
            List<Point> pointRankingList = pointRepository.findAllByOrderByPointDesc();
            log.info("[PointServiceImpl] 포인트랭킹조회: 사용자 포인트 랭킹: {}", pointRankingList);

            List<PointWithNickname> result = new ArrayList<>();
            for (Point point : pointRankingList) {
                PointWithNickname resPoint = PointWithNickname.builder()
                        .point(point.getPoint())
                        .nickname(point.getMember().getNickname())
                        .build();
                result.add(resPoint);
            }
            log.info("[PointServiceImpl] 포인트랭킹조회: 응답으로 전달할 사용자 포인트 랭킹 목록: {}", result);

            PointRanking responseRanking = PointRanking.builder()
                    .pointRanking(result)
                    .build();
            log.info("[PointServiceImpl] 포인트랭킹조회: 포인트 랭킹 조회 응답목록: {}", responseRanking);

            return GetPointRankingResponse.builder()
                    .code(HttpStatus.OK.toString())
                    .msg("정상적으로 포인트 랭킹을 조회하였습니다.")
                    .data(responseRanking)
                    .build();
        } catch (Exception e) {
            log.info("[PointServiceImpl] 포인트랭킹조회: 포인트 랭킹을 조회하던 중 문제가 발생했습니다.");
            e.printStackTrace();
            return GetPointRankingResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("포인트 랭킹을 조회하던 중 문제가 발생했습니다.")
                    .build();
        }
    }
}
