package com.developers.dmaker.service;

import com.developers.dmaker.dto.CreateDeveloper;
import com.developers.dmaker.dto.DeveloperDtailDto;
import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.exception.DMakerErrorCode;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.repository.DevelpoerRepository;
import com.developers.dmaker.repository.RetiredDevelpoerRepository;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static com.developers.dmaker.type.DeveloperLevel.SENIOR;
import static com.developers.dmaker.type.DeveloperSkillType.BACK_END;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author : Dmyun
 */
//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

//    @Autowired
//    DMakerService dMakerService;

//    @Test
//    public void testSomething() {
//
//        dMakerService.createDeveloper(CreateDeveloper.Request.builder()
//                                .developerLevel(DeveloperLevel.SENIOR)
//                                .developerSkillType(DeveloperSkillType.FRONT_END)
//                                .experienceYears(12)
//                                .memberId("memberId")
//                                .name("name")
//                                .age(32)
//                                .build());
//
//        List<DeveloperDto> allEmployDevelopers = dMakerService.getAllEmployDevelopers();
//        System.out.println(allEmployDevelopers);
//
//    }

    @Mock
    private DevelpoerRepository develpoerRepository;

    @Mock
    private RetiredDevelpoerRepository retiredDevelpoerRepository;

    @InjectMocks
    private DMakerService dMakerService;


    private final Developer defaultDeveloper =
            Developer.builder()
                .developerLevel(SENIOR)
                .developerSkillType(DeveloperSkillType.FRONT_END)
                .experienceYears(12)
                .name("name")
                .age(12)
                .build();

    private final CreateDeveloper.Request defaultCreateRequest =CreateDeveloper.Request.builder()
            .memberId("memberId")
            .age(32)
            .name("name")
            .experienceYears(12)
            .developerLevel(SENIOR)
            .developerSkillType(BACK_END)
            .build();


    @Test
    public void testSomething(){

        // given
        given(develpoerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));


        // when
        DeveloperDtailDto developerDtailDto = dMakerService.getDeveloperDetail("memberId");

        // then
        assertEquals(SENIOR, developerDtailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END,developerDtailDto.getDeveloperSkillType());
        assertEquals(12, developerDtailDto.getExperienceYears());
    }

    @Test
    public void createDeveloperTest_Success(){
        //given
        given(develpoerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);


        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultCreateRequest);

        //then
        verify(develpoerRepository, times(1))
                .save(captor.capture());

        Developer saveDeveloper = captor.getValue();
        assertEquals(SENIOR, saveDeveloper.getDeveloperLevel());
        assertEquals(BACK_END, saveDeveloper.getDeveloperSkillType());
        assertEquals(12, saveDeveloper.getExperienceYears());

    }

    @Test
    public void createDeveloperTest_Fail_Duplicated(){
        //given

        given(develpoerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        //when

        //then
        DMakerException dMakerException = assertThrows(DMakerException.class, ()-> {
            dMakerService.createDeveloper(defaultCreateRequest);
        });

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());

    }

}