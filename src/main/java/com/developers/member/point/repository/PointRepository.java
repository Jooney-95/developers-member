package com.developers.member.point.repository;

import com.developers.member.member.entity.Member;
import com.developers.member.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
    Point findByMember(Member member);
}
