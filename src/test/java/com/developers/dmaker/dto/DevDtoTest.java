package com.developers.dmaker.dto;

import org.junit.jupiter.api.Test;

/**
 * @author Fall
 */
class DevDtoTest {

    @Test
    void test() {

        DevDto devDto =  DevDto.builder()
                .name("Dongmin")
                .age(26)
                .build();

        devDto.printLog();
    }

}