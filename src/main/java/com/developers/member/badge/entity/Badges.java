package com.developers.member.badge.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Badges {
    BEGINNER_SOLVER("BEGINNER_SOLVER", "초보 해결사"),
    INTERMEDIATE_SOLVER("INTERMEDIATE_SOLVER", "중급 해결사"),
    ADVANCED_SOLVER("ADVANCED_SOLVER", "고급 해결사"),
    BEGINNER_MENTOR("BEGINNER_MENTOR", "초보 지도사"),
    INTERMEDIATE_MENTOR("INTERMEDIATE_MENTOR", "중급 지도사"),
    ADVANCED_MENTOR("ADVANCED_MENTOR", "고급 지도사"),
    BEGINNER_MENTEE("BEGINNER_MENTEE", "초보 참여자"),
    INTERMEDIATE_MENTEE("INTERMEDIATE_MENTEE", "중급 참여자"),
    ADVANCED_MENTEE("ADVANCED_MENTEE", "고급 참여자"),
    BEGINNER("BEGINNER", "비기너"),
    JUNIOR("JUNIOR", "주니어"),
    JUNGNIOR("JUNGNIOR", "중니어"),
    SENIOR("SENIOR", "시니어"),
    EMPTY("EMPTY", "칭호 없음"),
    ;

    private final String key;
    private final String value;
}
