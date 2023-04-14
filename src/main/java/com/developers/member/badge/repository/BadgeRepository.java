package com.developers.member.badge.repository;

import com.developers.member.badge.entity.Badge;
import com.developers.member.badge.entity.Badges;
import com.developers.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByMember(Member member);

    boolean existsByMemberAndBadgeName(Member member, Badges archieveBadge);
}
