package com.example.ecommerceproject.dto;

public class GenericResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private String errorCode;
    public GenericResponse() {
    }

    public GenericResponse(boolean success, T data, String message, String errorCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }
    // Success Response
    public static <T> GenericResponse<T> success(T data, String message) {
        return new GenericResponse<>(true, data, message, null);
    }
    // Error Response
    public static <T> GenericResponse<T> error(String message, String errorCode) {
        return new GenericResponse<>(false, null, message, errorCode);
    }
    // Getters & Setters
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

