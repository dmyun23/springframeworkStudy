package com.developers.dmaker.controller;

import com.developers.dmaker.dto.*;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDto> getAllDevelopers() {

        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllEmployDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDtailDto getDeveloperDetail(@PathVariable String memberId) {

        log.info("GET /developers HTTP/1.1");

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDtailDto editDeveloper(  @PathVariable String memberId
                                            ,@Valid @RequestBody EditDeveloper.Request request) {

        log.info("Put /developers HTTP/1.1");

        return dMakerService.editDeveloper(memberId,request);
    }

    @DeleteMapping("/developer/{memberId}")
    public DeveloperDtailDto deleteDeveloper(  @PathVariable String memberId ) {

        log.info("Delete /developers HTTP/1.1");

        return dMakerService.deleteDeveloper(memberId);
    }

    @PostMapping("/create-developer")
    public CreateDeveloper.Response createDevelopers(@Valid @RequestBody CreateDeveloper.Request request) {

        log.info("request : {}", request);
        return dMakerService.createDeveloper(request);
    }

    //RestControllerAdvice인 DMakerExceptionHandler에서 예외 처리를 한다.
//    @ResponseStatus(HttpStatus.CONFLICT)
//    @ExceptionHandler(DMakerException.class)  // 글로벌 에러처리 기법
//    public DMakerErrorResponse handlerException(DMakerException e,
//                                                HttpServletRequest request) {
//        log.error("errorCode: {}, url: {}, message:{}",
//                        e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage() );
//
//        return DMakerErrorResponse.builder()
//                .errorCode(e.getDMakerErrorCode())
//                .errorMessage(e.getDetailMessage())
//                    .build();
//    }
}
