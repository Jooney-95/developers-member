package com.developers.member.badge.entity;

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
@SQLDelete(sql = "UPDATE mybadge SET deleted = true WHERE mybadge_id = ?")
@Where(clause = "deleted = false")
@Table(name = "my_badge")
@Entity
public class MyBadge extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_badge_id", nullable = false)
    private Long mybadgeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_name", nullable = false)
    private Badges badgeName;

    @Builder
    public MyBadge(Member member, Badges badgeName) {
        this.member = member;
        this.badgeName = badgeName;
    }
    public String getBadgeKey() {
        return this.badgeName.getKey();
    }
    public void changeBadge(Badges badge) {
        this.badgeName = badge;
    }
}
