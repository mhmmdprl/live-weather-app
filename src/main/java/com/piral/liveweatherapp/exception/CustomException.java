package com.piral.liveweatherapp.exception;

import com.piral.liveweatherapp.enums.ResourceBundleEnum;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException{

    protected final HttpStatus httpStatus;
    protected final Object[] values;

    public CustomException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.values = null;
    }

    public CustomException(ResourceBundleEnum resourceBundleEnum, HttpStatus httpStatus, Object... values) {
        super(resourceBundleEnum.getValue());
        this.httpStatus = httpStatus;
        this.values = values;
    }
   
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

  
    public Object[] getValues() {
        return this.values;
    }
}
