package com.ea.crm.dataprovider.dto;


import com.ea.crm.dataprovider.exceptions.IErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shashank Bhattarai on 12/17/2022.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private Long id;
    private Integer status;
    private Integer code;
    private String title;
    // details just contains the message of the exception
    private String detail;
    // complete stack trace of the exception for logging into database
    private String stackTrace;
    private Map<String, Object> meta;

    public ErrorDTO() {
        this.id = Instant.now().toEpochMilli();
    }

    public ErrorDTO(IErrorCode errorCode, String errorMessage) {
        this.id = Instant.now().toEpochMilli();
        this.code = errorCode.getCode();
        this.title = errorMessage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        if (!meta.isEmpty()) {
            this.meta = meta;
        }
    }

    public ErrorDTO addMeta(String metaKey, Object data) {
        if (meta == null) {
            meta = new HashMap<>();
        }
        if (data != null) {
            meta.put(metaKey, data);
        }
        return this;
    }
}