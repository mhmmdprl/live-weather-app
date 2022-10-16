package com.piral.liveweatherapp.util;

import com.piral.liveweatherapp.dto.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseMessage prepareResponseMessage(Boolean success, ResponseMessage.Type type, Object message, Object result) {
        if (result != null) {
            return new ResponseMessage(success, type, message, result);
        }
        return new ResponseMessage(success, type, message);
    }

    public static ResponseEntity<ResponseMessage> getSuccessResponse(Object result) {
        return new ResponseEntity<>(prepareResponseMessage(Boolean.TRUE, ResponseMessage.Type.SUCCESS, null, result), HttpStatus.OK);
    }
}
