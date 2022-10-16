package com.piral.liveweatherapp.dto.response;

public class ResponseMessage<T> {

    private Boolean success;
    private Type type;
    private Object message; // NOSONAR
    private T result;

    public ResponseMessage() {

    }

    public ResponseMessage(Boolean success, ResponseMessage.Type type, Object message) {
        this.success = success;
        this.type = type;
        this.message = message;
    }

    public ResponseMessage(Boolean success, ResponseMessage.Type type, Object message, T result) {
        this.success = success;
        this.type = type;
        this.message = message;
        this.result = result;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public enum Type {
        SUCCESS, WARNING, ERROR, INFO
    }
}
