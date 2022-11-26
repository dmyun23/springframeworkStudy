package com.developers.dmaker.service;

import com.developers.dmaker.code.StatusCode;
import com.developers.dmaker.dto.*;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.entity.RetiredDeveloper;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.repository.DevelpoerRepository;
import com.developers.dmaker.repository.RetiredDevelpoerRepository;
import com.developers.dmaker.type.DeveloperLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.developers.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {

    private final DevelpoerRepository develpoerRepository;
    private final RetiredDevelpoerRepository retiredDeveloperRepository;

    // ACID
    // Atomic  Consistency  Isolation  Durability
    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {

        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                        .memberId(request.getMemberId())
                        .developerLevel(request.getDeveloperLevel())
                        .developerSkillType(request.getDeveloperSkillType())
                        .experienceYears(request.getExperienceYears())
                        .name(request.getName())
                        .age(request.getAge())
                        .statusCode(StatusCode.EMPLOYED)
                        .build();

        develpoerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {

        // business validation
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        develpoerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer->{
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    public List<DeveloperDto> getAllEmployDevelopers() {

        return develpoerRepository.findDevelopersByStatusCode(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDtailDto getDeveloperDetail(String memberId) {
        return develpoerRepository.findByMemberId(memberId)
                .map(DeveloperDtailDto::fromEntity)
                .orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDtailDto editDeveloper(String memberId, EditDeveloper.Request request) {

        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        Developer developer = develpoerRepository.findByMemberId(memberId).orElseThrow(
                                                    () -> new DMakerException(NO_DEVELOPER));

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        developer.setAge(request.getAge());

        return DeveloperDtailDto.fromEntity(developer);
    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if(developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10 ) {
//            throw new RuntimeException("SENIOR need 10 years experience.");
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if(developerLevel == DeveloperLevel.JUNGNIOR
                && (experienceYears < 4 || experienceYears >10 )){
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }

        if(developerLevel == DeveloperLevel.JUNIOR && experienceYears >4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDtailDto deleteDeveloper(String memberId) {

        Developer developer =develpoerRepository.findByMemberId(memberId)
                                 .orElseThrow( ()->new DMakerException(NO_DEVELOPER));
        developer.setStatusCode(StatusCode.RETIRED);
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                                                .memberId(memberId)
                                                .name(developer.getName())
                                                .build();

        retiredDeveloperRepository.save(retiredDeveloper);

        return DeveloperDtailDto.fromEntity(developer);
    }
}
