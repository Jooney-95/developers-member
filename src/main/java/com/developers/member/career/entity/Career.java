package com.developers.member.career.entity;

import com.developers.member.common.entity.BaseTimeEntity;
import com.developers.member.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString(exclude = {"member"})
@SQLDelete(sql = "UPDATE career SET deleted = true WHERE career_id = ?")
@Where(clause = "deleted = false")
@Table(name = "career")
@Entity
public class Career extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "career_id", nullable = false)
    private Long careerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "career_start", nullable = false)
    private LocalDate start;

    @Column(name = "career_end", nullable = false)
    private LocalDate end;

    @Column(name = "company", nullable = false)
    private String company;

    @Builder
    public Career(Member member, LocalDate start, LocalDate end, String company) {
        this.member = member;
        this.start = start;
        this.end = end;
        this.company = company;
    }

    public void updateCompany(String company) {
        this.company = company;
    }
    public void changeStartDate(LocalDate start) {
        this.start = start;
    }
    public void changeEndDate(LocalDate end) {
        this.end = end;
    }
}
