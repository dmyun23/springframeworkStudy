package com.developers.dmaker.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeveloperSkillType {

    BACK_END("백엔드 개발자"),
    FRONT_END("백엔드 개발자"),
    FULL_STACK("백엔드 개발자");

    private final String description;
}
