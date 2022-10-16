package com.piral.liveweatherapp.aspect;

import com.piral.liveweatherapp.dto.response.ResponseMessage;
import com.piral.liveweatherapp.enums.ResourceBundleEnum;
import com.piral.liveweatherapp.exception.CustomException;
import com.piral.liveweatherapp.util.ResourceBundleUtils;
import com.piral.liveweatherapp.util.ResponseUtils;
import java.util.Locale;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class WeatherAuditAspect {

    private final Logger LOGGER = LoggerFactory.getLogger(WeatherAuditAspect.class);

    @Around("execution(* com.piral.liveweatherapp.controller.*Controller.*(..)) ")
    public Object aroundOperations(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Locale locale = new Locale("tr", "TR");
        Object targetObject;
        try {
            Object joinPointObject = proceedingJoinPoint.proceed();
            targetObject = ResponseUtils.getSuccessResponse(joinPointObject);
        } catch (CustomException e) {
            String message = e.getMessage();

            String responseMessage = null;
            HttpStatus responseHttpStatus = getHttpStatusFromException(e);

            if (message != null) {
                try {
                    if (e.getValues() != null) {
                        responseMessage = ResourceBundleUtils.getMessageFromBundle(locale, message, e.getValues());
                    } else {
                        responseMessage = ResourceBundleUtils.getMessageFromBundle(locale, message);
                    }
                } catch (Exception e2) {
                    responseMessage = message;
                }
            }
            this.LOGGER.error("CustomException =>", responseMessage);
            targetObject = new ResponseEntity<>(ResponseUtils.prepareResponseMessage(
                Boolean.FALSE, ResponseMessage.Type.ERROR, responseMessage, null), responseHttpStatus);
        } catch (Exception t) {

            String responseMessage = ResourceBundleUtils.getMessageFromBundle(locale, ResourceBundleEnum.UNEXPECTED_ERROR.getValue());
            HttpStatus responseHttpStatus = getHttpStatusFromException(t);
            this.LOGGER.error("Exception =>", responseMessage);
            targetObject = new ResponseEntity<>(
                ResponseUtils.prepareResponseMessage(Boolean.FALSE, ResponseMessage.Type.ERROR, responseMessage, null), responseHttpStatus);
        }
        return targetObject;
    }


    private static HttpStatus getHttpStatusFromException(Throwable argT) {
        return argT instanceof CustomException ? ((CustomException) argT).getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
