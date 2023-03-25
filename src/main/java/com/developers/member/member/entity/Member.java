package com.developers.member.member.entity;

import com.developers.member.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @DynamicInsert과 @DynamicUpdate 어노테이션 적용 여부 논의 필요
 */

//@DynamicUpdate
//@DynamicInsert
@NoArgsConstructor
@Getter
@ToString
@Table(name = "member")
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "profile_image_url", nullable = false)
    private String profileImageUrl;

    @Column(name = "address")
    private String address;

    @Column(name = "is_mentor", nullable = false)
    private boolean isMentor;

    @Column(name = "introduce")
    private String introduce;

    @Column(name = "position")
    private String position;

    @Column(name = "skills")
    private String skills;

    @Builder
    public Member(String email, String nickname, String password, Type type, Role role, String profileImageUrl, String address, boolean isMentor, String introduce, String position, String skills) {
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
}
