package com.developers.member.badge.repository;


import com.developers.member.badge.entity.MyBadge;
import com.developers.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBadgeRepository extends JpaRepository<MyBadge, Long> {
    MyBadge findByMember(Member member);
}
