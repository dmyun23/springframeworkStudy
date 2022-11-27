package com.developers.dmaker.controller;

import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.service.DMakerService;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author : Dmyun
 */

@WebMvcTest(DMakerController.class)  // Controller 관련된 Bean 들을 올려줌. , 매개변수를 넣어 주면 특정 Controller만 ,
               // ControllAdvice, Filter 등  컨트롤러 진입하기 위한 경로가 되는 기반이 따라 올라옴.
class DMakerControllerTest {

    // Controller 내 메소드에 요청하기 위한 어노테이션( http 프로토콜 통신 기반 테스트 요청 )
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DMakerService dMakerService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(),
                    MediaType.APPLICATION_JSON.getSubtype(),
                    StandardCharsets.UTF_8
                    );

    @Test
    void getAllDevelopers() throws Exception {

        DeveloperDto juniorDeveloperDto = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.JUNIOR)
                .developerSkillType(DeveloperSkillType.BACK_END)
                .memberId("memberId1")
                .build();

        DeveloperDto seniorDeveloperDto = DeveloperDto.builder()
                .developerLevel(DeveloperLevel.SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .memberId("memberId2")
                .build();

        given(dMakerService.getAllEmployDevelopers())
                .willReturn(Arrays.asList(juniorDeveloperDto, seniorDeveloperDto));

        mockMvc.perform(get("/developers").contentType(contentType))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].developerSkillType",
                                is(DeveloperSkillType.BACK_END.name())))
                .andExpect(jsonPath("$.[0].developerLevel",
                                is(DeveloperLevel.JUNIOR.name())))
                .andExpect(jsonPath("$.[1].developerSkillType",
                                is(DeveloperSkillType.FRONT_END.name())))
                .andExpect(jsonPath("$.[1].developerLevel",
                                                is(DeveloperLevel.SENIOR.name())));

    }
}

/**
 @MockBean이 붙으면 mock객체를 스프링 컨텍스트에 등록하게 된다.
 만약 application context에 같은 타입의 빈이 존재한다면, 해당 빈을 mock으로 교체한다.
 --------------------------------------------------------------------------
 Mock 종류        의존성 주입 Target
 @Mock              @InjectMocks
 @MockBean          @SpringBootTest
 --------------------------------------------------------------------------
 만약 통합테스트를 진행하면 @MockBean을 사용하면 될 것이고, 여타 다른 spring 빈들이 필요없고 특정 빈들만 mock으로 가지고 있으면 된다면
 @Mock을 이용한 테스트를 진행하면 될 것이다.
 */