package com.developers.member.point.entity;

import com.developers.member.common.entity.BaseTimeEntity;
import com.developers.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
@SQLDelete(sql = "UPDATE point SET deleted = true WHERE point_id = ?")
@Where(clause = "deleted = false")
@Table(name = "point")
@Entity

public class Point extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id", nullable = false)
    private Long pointId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "point", nullable = false)
    private Long point;

    @Builder
    public Point(Member member, Long point) {
        this.member = member;
        this.point = point;
    }

    public void increase(Long point) {
        this.point += point;
    }

    public void decrease(Long point) {
        this.point -= point;
        if(this.point < 0) {
            this.point = 0L;
        }
    }
}
