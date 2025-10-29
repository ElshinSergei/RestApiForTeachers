package org.example.restapiforteachers.model;

import java.time.LocalDate;
import java.util.List;

public class ErrorResponse {
    private LocalDate timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<String> errors;

    public ErrorResponse(Integer status, String error, String message, List<String> errors) {
        this.timestamp = LocalDate.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }

    public ErrorResponse(Integer status, String error, String message) {
        this.timestamp = LocalDate.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
