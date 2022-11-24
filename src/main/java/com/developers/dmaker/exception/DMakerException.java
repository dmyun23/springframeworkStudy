package com.developers.dmaker.exception;

import lombok.Getter;

@Getter
public class DMakerException extends RuntimeException {

    private DMakerErrorCode dMakerErrorCode;
    private String detailMessage;

    public DMakerException(DMakerErrorCode errorCode) {
        super(errorCode.getMeesgae());
        this.dMakerErrorCode = errorCode;
        this.detailMessage = errorCode.getMeesgae();
    }

    public DMakerException(DMakerErrorCode errorCode, String detailMessage) {
        super(detailMessage);
        this.dMakerErrorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
