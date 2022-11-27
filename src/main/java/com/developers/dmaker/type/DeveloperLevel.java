package com.developers.dmaker.type;

import com.developers.dmaker.entity.constant.DMakerConstant;
import com.developers.dmaker.exception.DMakerErrorCode;
import com.developers.dmaker.exception.DMakerException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

import static com.developers.dmaker.entity.constant.DMakerConstant.*;
import static com.developers.dmaker.exception.DMakerErrorCode.*;

@AllArgsConstructor
@Getter
public enum DeveloperLevel {

    //    NEW("신입 개발자", 0,0),
//    JUNIOR("주니어 개발자", 1, MAX_JUNIOR_EXPERIENCE_YEARS),
//    JUNGNIOR("중니어 개발자", MAX_JUNIOR_EXPERIENCE_YEARS+1, MIN_SENIOR_EXPERIENCE_YEARS -1 ),
//    SENIOR("시니어 개발자", MIN_SENIOR_EXPERIENCE_YEARS, 70)
//    ;
    NEW("신입 개발자", years->years==0),
    JUNIOR("주니어 개발자", years->years<=MAX_JUNIOR_EXPERIENCE_YEARS),
    JUNGNIOR("중니어 개발자", years->years>MAX_JUNIOR_EXPERIENCE_YEARS
            && years<MIN_SENIOR_EXPERIENCE_YEARS ),
    SENIOR("시니어 개발자", years->years>=MIN_SENIOR_EXPERIENCE_YEARS)
    ;

    private final String  description;
    private Function<Integer,Boolean> validFunction;

    public void validateExperience(Integer experienceYears){
        if(!validFunction.apply(experienceYears)) {
            throw new  DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
//    private final Integer minExperienceYears;
//    private final Integer maxExperienceYears;
}
