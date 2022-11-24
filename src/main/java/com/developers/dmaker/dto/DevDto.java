package com.developers.dmaker.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author Fall
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j  // log
public class DevDto {

    String name;
    Integer age;
    LocalDateTime startAt;

    public void printLog(){
        log.info(getName());
    }
}
