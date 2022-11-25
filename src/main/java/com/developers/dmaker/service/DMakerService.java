package com.developers.dmaker.service;

import com.developers.dmaker.dto.CreateDeveloper;
import com.developers.dmaker.dto.DeveloperDtailDto;
import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.exception.DMakerErrorCode;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.repository.DevelpoerRepository;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.developers.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {

    private final DevelpoerRepository develpoerRepository;

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
                        .build();

        develpoerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        // business validation
        DeveloperLevel developerLevel = request.getDeveloperLevel();
        Integer experienceYears = request.getExperienceYears();

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
        develpoerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer->{
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    public List<DeveloperDto> getAllDevelopers() {

        return develpoerRepository.findAll()
                .stream().map(DeveloperDto::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDtailDto getDeveloperDetail(String memberId) {
        return develpoerRepository.findByMemberId(memberId)
                .map(DeveloperDtailDto::fromEntity)
                .orElseThrow(()->new DMakerException(NO_DEVELOPER));
    }
}
