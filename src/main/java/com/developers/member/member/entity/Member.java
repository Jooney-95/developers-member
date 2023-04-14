package com.developers.member.member.entity;

import com.developers.member.badge.entity.Badges;
import com.developers.member.badge.entity.MyBadge;
import com.developers.member.badge.entity.Badge;
import com.developers.member.career.entity.Career;
import com.developers.member.common.entity.BaseTimeEntity;
import com.developers.member.point.entity.Point;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 엔티티 클래스
 * 사용자(Member)와 포인트(Point: 1대1 관계
 * 사용자(Member)와 선택 칭호(MyBadge): 1대1 관계
 * 사용자(Member)와 획득 칭호(MyBadge): 1대다 관계
 * 사용자(Member)와 경력 정보(Career): 1대다 관계
 */
@NoArgsConstructor
@Getter
@ToString(exclude = {"point"})
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE member_id = ?")
@Where(clause = "deleted = false")
@Table(name = "member")
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "address")
    private String address;

    @Column(name = "is_mentor", nullable = false)
    private boolean isMentor = false;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "position")
    private String position;

    @Column(name = "skills")
    private String skills;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 255, nullable = true)
    private String refreshToken;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Point point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Career> careers = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private MyBadge myBadge;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Badge> badge = new ArrayList<>();


    @Builder
    public Member(String email, String nickname, String password, Type type, Role role, String profileImageUrl, String address, boolean isMentor, String introduce, String position, String skills, Long point) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.type = type;
        this.role = role;
        this.profileImageUrl = profileImageUrl;
        this.address = address;
        this.isMentor = isMentor;
        this.introduce = introduce;
        this.position = position;
        this.skills = skills;
        this.point = Point.builder().member(this).point(point).build();
    }

    public void updateNickname(String nickname) {
        if (nickname != null) {
            this.nickname = nickname;
        }
    }
    public void changePassword(String password) {
        if (password != null) {
            this.password = password;
        }
    }
    public void updateProfileImageUrl(String profileImageUrl) {
        if (profileImageUrl != null) {
            this.profileImageUrl = profileImageUrl;
        }
    }
    public void applyMentor(boolean isMentor) {
        this.isMentor = isMentor;
    }
    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public void updatePosition(String position) {
        this.position = position;
    }
    public void updateSkills(String skills) {
        this.skills = skills;
    }
    public void updateAddress(String address) { this.address = address; }
    public void increasePoint(Long point) {
        this.point.increase(point);
    }
    public void decreasePoint(Long point) {
        this.point.decrease(point);
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Member update(String email, String nickname, Type type) {
        this.email = email;
        this.nickname = nickname;
        this.type = type;
        return this;
    }
    public void setPoint(Long point) {
        this.point = Point.builder().member(this).point(point).build();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setMyBadge(MyBadge myBadge) {
        this.myBadge = myBadge;
    }
}
