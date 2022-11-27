package com.developers.dmaker.service;

import com.developers.dmaker.code.StatusCode;
import com.developers.dmaker.dto.*;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.entity.RetiredDeveloper;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.repository.DevelpoerRepository;
import com.developers.dmaker.repository.RetiredDevelpoerRepository;
import com.developers.dmaker.type.DeveloperLevel;
import lombok.NonNull;
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
    @Transactional
    public CreateDeveloper.Response createDeveloper(
            CreateDeveloper.Request request
    ) {
        validateCreateDeveloperRequest(request);
        // business logic start
        return CreateDeveloper.Response.fromEntity(
                develpoerRepository.save(createDeveloperFromRequest(request))
        );
    }
    @Transactional(readOnly = true)
    public List<DeveloperDto> getAllEmployDevelopers()
    {
        return develpoerRepository.findDevelopersByStatusCode(StatusCode.EMPLOYED)
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public DeveloperDtailDto getDeveloperDetail(
            String memberId
    ) {
        return DeveloperDtailDto.fromEntity(getDeveloperByMemberId(memberId));
    }
    @Transactional
    public DeveloperDtailDto editDeveloper(
            String memberId, EditDeveloper.Request request
    ) {
        validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
        return DeveloperDtailDto.fromEntity(
                getDeveloperUpdateFromRequest(
                        request, getDeveloperByMemberId(memberId)
                )
        );
    }

    private Developer getDeveloperUpdateFromRequest(
            EditDeveloper.Request request, Developer developer
    ) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());
        developer.setAge(request.getAge());
        return developer;
    }

    @Transactional
    public DeveloperDtailDto deleteDeveloper(
            String memberId
    ) {
        Developer developer =getDeveloperByMemberId(memberId);
        developer.setStatusCode(StatusCode.RETIRED);
        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                                                .memberId(memberId)
                                                .name(developer.getName())
                                                .build();
        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDtailDto.fromEntity(developer);
    }
    private Developer getDeveloperByMemberId(String memberId){
        return develpoerRepository.findByMemberId(memberId)
                .orElseThrow( ()->new DMakerException(NO_DEVELOPER));
    }
    private void validateCreateDeveloperRequest(
            @NonNull  CreateDeveloper.Request request
    ){
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
        develpoerRepository.findByMemberId(
                        request.getMemberId()
                    ).ifPresent((developer->{
            throw new DMakerException(DUPLICATED_MEMBER_ID);
        }));
    }
    private static void validateDeveloperLevel(
            DeveloperLevel developerLevel, Integer experienceYears
    ) {
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
    private Developer createDeveloperFromRequest(CreateDeveloper.Request request){
        return Developer.builder()
                .memberId(request.getMemberId())
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .name(request.getName())
                .age(request.getAge())
                .statusCode(StatusCode.EMPLOYED)
                .build();
    }
}
