package com.developers.dmaker.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum StatusCode {

    EMPLOYED("고용"),
    RETIRED("퇴직");

    private final String description;
}
