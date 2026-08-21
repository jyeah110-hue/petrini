package com.petcare.petcare.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseExceptionHandler extends RuntimeException {
    private final int status;
    private final String responseBody;

    public ResponseExceptionHandler(int status, String responseBody) {
        super("API 오류: HTTP " + status);
        this.status = status;
        this.responseBody = responseBody;
    }
}
