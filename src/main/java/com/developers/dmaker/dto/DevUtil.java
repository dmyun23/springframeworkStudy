package com.developers.dmaker.dto;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DevUtil {

    public static void printTimeLog(){
        System.out.println(LocalDateTime.of(2022,11,23,10,50,16));
    }

    public static void printNowLog(){
        System.out.println(LocalDateTime.now());
    }

}
