package com.developers.member.career.repository;

import com.developers.member.career.entity.Career;
import com.developers.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerRepository extends JpaRepository<Career, Long> {
    List<Career> findByMember(Member member);

    void deleteByMember(Member member);
}
