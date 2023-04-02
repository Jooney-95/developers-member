package com.developers.member.member.repository;

import com.developers.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * findByIsMentorIsAndMemberId: 멘토로 등록된 사용자의 정보 조회
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIsMentorIsAndMemberId(boolean isMentor, Long memberId);

    boolean existsByNickname(String nickname);

    Optional<Member> findByNickname(String nickname);
}
