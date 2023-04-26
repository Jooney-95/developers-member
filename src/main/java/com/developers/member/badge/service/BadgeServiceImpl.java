package com.developers.member.badge.service;

import com.developers.member.badge.dto.request.MemberChangeBadgeRequest;
import com.developers.member.badge.dto.request.MemberSaveBadgeRequest;
import com.developers.member.badge.dto.response.*;
import com.developers.member.badge.entity.Badge;
import com.developers.member.badge.entity.Badges;
import com.developers.member.badge.entity.MyBadge;
import com.developers.member.badge.repository.BadgeRepository;
import com.developers.member.badge.repository.MyBadgeRepository;
import com.developers.member.member.dto.response.MemberIdResponse;
import com.developers.member.member.entity.Member;
import com.developers.member.member.repository.MemberRepository;
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
public class BadgeServiceImpl implements BadgeService {
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;
    private final MyBadgeRepository myBadgeRepository;

    @Override
    public MemberAllBadgeResponse getMyAllBadge(Long memberId) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isPresent()) {
                // 획득 칭호 목록의 경우 DTO로 감싸서 전달
                List<Badge> badges = badgeRepository.findByMember(member.get());
                List<MyBadgeList> myBadgeList = new ArrayList<>();
                for(Badge badge : badges) {
                    MyBadgeList m = MyBadgeList.builder()
                            .badge(String.valueOf(badge.getBadgeName()))
                            .build();
                    myBadgeList.add(m);
                }
                log.info("[BadgeServiceImpl] 획득칭호목록조회: {}번 사용자 획득 칭호 목록(myBadgeList): {}", memberId, myBadgeList);
                if(myBadgeList.size() == 0) {
                    log.info("[BadgeServiceImpl] 획득칭호목록조회: {}번 사용자는 획득한 칭호가 없습니다.", memberId);
                    return MemberAllBadgeResponse.builder()
                            .code(HttpStatus.NOT_FOUND.toString())
                            .msg("해당 사용자는 획득한 칭호가 없습니다.")
                            .data(null)
                            .build();
                }
                MemberIdWithAllBadgeResponse memberBadgeInfo = MemberIdWithAllBadgeResponse.builder()
                        .memberId(memberId)
                        .myBadgeList(myBadgeList)
                        .build();
                return MemberAllBadgeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 사용자의 획득한 칭호 목록을 조회했습니다.")
                        .data(memberBadgeInfo)
                        .build();
            } else {
                log.info("[BadgeServiceImpl] 획득칭호목록조회: {}번 사용자는 존재하지 않는 사용자입니다.", memberId);
                return MemberAllBadgeResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[BadgeServiceImpl] 획득칭호목록조회: 사용자의 칭호를 조회하던 중 문제가 발생했습니다.");
            return MemberAllBadgeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("사용자의 칭호를 조회하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberPickBadgeResponse getMyPickBadge(Long memberId) {
        try {
            Optional<Member> member = memberRepository.findById(memberId);
            if (member.isPresent()) {
                // 현재 착용한 칭호와 획득한 모든 칭호 목록
                MyBadge mybadge = member.get().getMyBadge();
                String response = "";
                log.info("[BadgeServiceImpl] 착용칭호조회: {}번 사용자가 착용한 칭호는 {}입니다.", memberId, mybadge);
                if(mybadge == null) {
                    log.info("[BadgeServiceImpl] 착용칭호조회: {}번 사용자는 칭호를 착용하지 않았습니다.", memberId);
                    return MemberPickBadgeResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .msg("해당 사용자는 칭호를 착용하지 않았습니다.")
                            .data(null)
                            .build();
                }
                response = mybadge.getBadgeKey();
                MemberIdWithPickBadgeResponse res = MemberIdWithPickBadgeResponse.builder()
                        .memberId(memberId)
                        .myBadge(response)
                        .build();
                log.info("[BadgeServiceImpl] 착용칭호조회: 사용자 착용칭호 조회: " + res);
                return MemberPickBadgeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 사용자의 착용한 칭호 정보를 조회했습니다.")
                        .data(res)
                        .build();
            } else {
                log.info("[BadgeServiceImpl] 착용칭호조회: {}번 사용자는 존재하지 않는 사용자입니다.", memberId);
                return MemberPickBadgeResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[BadgeServiceImpl] 착용칭호조회: 사용자의 착용 칭호를 조회하던 중 문제가 발생했습니다.");
            return MemberPickBadgeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("사용자의 칭호를 조회하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Transactional
    @Override
    public MemberChangeBadgeResponse changeBadge(MemberChangeBadgeRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            if (member.isPresent()) {
                log.info("[BadgeServiceImpl] 착용칭호변경: {}번 사용자가 착용할 칭호는 {}입니다.", request.getMemberId(), request.getBadgeName());
                List<Badge> myBadges = badgeRepository.findByMember(member.get());
                log.info("[BadgeServiceImpl] 착용칭호변경: {}번 사용자의 획득 칭호 목록: {}", request.getMemberId(), myBadges);
                boolean badgeExists = false;
                for(Badge bage : myBadges) {
                    if(bage.getBadgeName().getKey().equals(request.getBadgeName())) {
                        log.info("[BadgeServiceImpl] 착용칭호변경: {} 칭호는 획득한 칭호이기에 착용이 가능합니다.", request.getBadgeName());
                        badgeExists = true;
                        break;
                    }
                }
                if(badgeExists == false) {
                    log.info("[BadgeServiceImpl] 착용칭호변경: {} 칭호는 획득하지 못한 칭호입니다.", request.getBadgeName());
                    return MemberChangeBadgeResponse.builder()
                            .code(HttpStatus.BAD_REQUEST.toString())
                            .msg("사용자가 획득하지 못한 칭호입니다.")
                            .data(null)
                            .build();
                }
                log.info("[BadgeServiceImpl] 착용칭호변경: 칭호 착용 로직을 실행합니다.");
//                member.get().getMyBadge().changeBadge(Badges.valueOf(request.getBadgeName()));
                MyBadge myBadge = MyBadge.builder()
                        .member(member.get())
                        .badgeName(Badges.valueOf(request.getBadgeName()))
                        .build();
                myBadgeRepository.save(myBadge);
                MemberIdWithPickBadgeResponse res = MemberIdWithPickBadgeResponse.builder()
                        .memberId(request.getMemberId())
                        .myBadge(request.getBadgeName())
                        .build();
                log.info("[BadgeServiceImpl] 착용칭호변경: 칭호 착용 여부를 DB에 반영 완료하였습니다. {}", res);
                return MemberChangeBadgeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 칭호를 변경했습니다.")
                        .data(res)
                        .build();
            } else {
                log.info("[BadgeServiceImpl] 착용칭호변경: {}번 사용자는 존재하지 않는 사용자입니다.", request.getMemberId());
                return MemberChangeBadgeResponse.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (Exception e) {
            log.info("[BadgeServiceImpl] 착용칭호변경: 사용자의 칭호를 변경하던 중 문제가 발생했습니다.");
            e.printStackTrace();
            return MemberChangeBadgeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("사용자의 칭호를 변경하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }

    @Override
    public MemberSaveBadgeResponse saveBadgeList(MemberSaveBadgeRequest request) {
        try {
            Optional<Member> member = memberRepository.findById(request.getMemberId());
            log.info("[BadgeServiceImpl] 칭호획득: {} 사용자가 {} 칭호 획득 요청을 했습니다.", request.getMemberId(), request.getArchieveBadge());
            if(member.isPresent()) {
                Badge badge = Badge.builder()
                        .member(member.get())
                        .badgeName(request.getArchieveBadge())
                        .build();
                boolean exist = badgeRepository.existsByMemberAndBadgeName(member.get(), request.getArchieveBadge());
                if(exist) {
                    log.info("[BadgeServiceImpl] 칭호획득: {} 칭호는 사용자가 이미 획득한 칭호입니다.", request.getArchieveBadge());
                    return MemberSaveBadgeResponse.builder()
                            .code(HttpStatus.ALREADY_REPORTED.toString())
                            .msg("해당 사용자가 이미 획득한 칭호입니다.")
                            .data(null)
                            .build();
                }
                badgeRepository.save(badge);
                MemberIdResponse memberId = MemberIdResponse.builder().memberId(request.getMemberId()).build();
                log.info("[BadgeServiceImpl] 칭호획득: {} 칭호는 사용자가 획득 가능합니다.", request.getArchieveBadge());
                return MemberSaveBadgeResponse.builder()
                        .code(HttpStatus.OK.toString())
                        .msg("정상적으로 획득한 칭호를 추가했습니다.")
                        .data(memberId)
                        .build();
            } else {
                log.info("[BadgeServiceImpl] 칭호획득: {}번 사용자는 존재하지 않는 사용자입니다.", request.getMemberId());
                return MemberSaveBadgeResponse.builder()
                        .code(HttpStatus.NOT_FOUND.toString())
                        .msg("존재하지 않는 사용자입니다.")
                        .data(null)
                        .build();
            }
        } catch (IllegalArgumentException e) {
            log.info("[BadgeServiceImpl] 칭호획득: {} 칭호는 존재하지 않는 칭호입니다.", request.getArchieveBadge());
            return MemberSaveBadgeResponse.builder()
                    .code(HttpStatus.BAD_REQUEST.toString())
                    .msg("획득한 칭호 값이 올바르지 않습니다.")
                    .data(null)
                    .build();
        } catch (Exception e) {
            log.info("[BadgeServiceImpl] 칭호획득: {} 라는 칭호를 추가하던 중 문제가 발생했습니다.", request.getArchieveBadge());
            return MemberSaveBadgeResponse.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                    .msg("획득한 칭호를 추가하던 중 문제가 발생했습니다.")
                    .data(null)
                    .build();
        }
    }
}
