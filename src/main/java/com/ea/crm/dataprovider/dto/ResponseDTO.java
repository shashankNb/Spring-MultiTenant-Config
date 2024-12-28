package com.ea.crm.dataprovider.dto;

import com.ea.crm.dataprovider.exceptions.IErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Response DTO conforming to JSON API Specification
 * <a href="https://jsonapi.org/format/">...</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {

    private Object data;
    private List<ErrorDTO> errors;
    private Map<String, Object> meta = new LinkedHashMap<>();

    public ResponseDTO() {

    }

    public static ResponseDTO errorResponse(IErrorCode errorCode, String errorMessage) {
        ResponseDTO responseDTO = new ResponseDTO();
        ErrorDTO error = new ErrorDTO(errorCode, errorMessage);
        responseDTO.addError(error);
        return responseDTO;
    }

    public static ResponseDTO errorResponse(IErrorCode errorCode) {
        ResponseDTO responseDTO = new ResponseDTO();
        ErrorDTO error = new ErrorDTO(errorCode, errorCode.getError());
        responseDTO.addError(error);
        return responseDTO;
    }

    public ResponseDTO(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public void addError(ErrorDTO errorDetails) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(errorDetails);

    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public ResponseDTO addMeta(String key, Object value) {
        meta.put(key, value);
        return this;
    }
}

