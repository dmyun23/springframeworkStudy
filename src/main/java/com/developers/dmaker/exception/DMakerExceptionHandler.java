package com.developers.dmaker.exception;

import com.developers.dmaker.dto.DMakerErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.developers.dmaker.exception.DMakerErrorCode.INTERNER_SERVER_ERROR;
import static com.developers.dmaker.exception.DMakerErrorCode.INVALID_REQUEST;

/**
 * @author Dmyun
 */
@Slf4j
@RestControllerAdvice  // 각 컨트롤러에 조언 역할을 하는 특수형태의 컨트롤러 이다.
public class DMakerExceptionHandler {

    @ExceptionHandler(DMakerException.class)  // 글로벌 에러처리 기법
    public DMakerErrorResponse handlerException(DMakerException e,
                                                HttpServletRequest request) {
        log.error("errorCode: {}, url: {}, message:{}",
                        e.getDMakerErrorCode(), request.getRequestURI(), e.getDetailMessage() );

        return DMakerErrorResponse.builder()
                .errorCode(e.getDMakerErrorCode())
                .errorMessage(e.getDetailMessage())
                    .build();
    }

    // 컨트롤러 내부로 진입하기 전에 발생하는 익셉션일 경우, Custom Exception을 적용하고 싶은 케이스에 사용
    @ExceptionHandler(value={
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentNotValidException.class
    })
    public DMakerErrorResponse handlerBadRequest(Exception e, HttpServletRequest request){
        log.error("url: {}, message:{}", request.getRequestURL(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMeesgae())
                .build();
    }

    // 기타 등등, 익셉션의 종류는 무수히 많고 프로젝트에서 사용하는 라이브러리 종류에 따른 예상치 못한 익셉션이 발생할 수 있다.
    // 예상할 수 없는 익셉션에 대한 처리.
    @ExceptionHandler(Exception.class)
    public DMakerErrorResponse handleException(Exception e, HttpServletRequest request){

        log.error("url: {}, message: {}", request.getRequestURL(), e.getMessage());

        return DMakerErrorResponse.builder()
                .errorCode(INTERNER_SERVER_ERROR)
                .errorMessage(INTERNER_SERVER_ERROR.getMeesgae())
                .build();
    }
}
