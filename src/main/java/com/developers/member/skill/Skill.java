package com.developers.member.skill;


import com.developers.member.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@Getter
@SQLDelete(sql = "UPDATE skill SET deleted = true WHERE skill_id = ?")
@Where(clause = "deleted = false")
@Table(name = "skill")
@Entity
public class Skill extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long skillId;

    @Column(name = "name")
    private String name;
}
