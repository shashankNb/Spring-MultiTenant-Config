package com.ea.crm.dataprovider.exceptions;






import com.ea.crm.dataprovider.dto.ErrorDTO;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shashank Bhattarai on 12/17/2022.
 */
public class DataServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Long timestamp;
    private IErrorCode iErrorCode;
    private ErrorDTO errorDTO;
    private Map<String,Object> meta = new LinkedHashMap<>();
    private Map<String,Object> privateMeta = new LinkedHashMap<>();

    public DataServiceException(Throwable cause){
        super(cause);
        this.timestamp = Instant.now().toEpochMilli();
    }

    public DataServiceException(ErrorDTO errorDTO){
        super(errorDTO.getDetail());
        this.timestamp = Instant.now().toEpochMilli();
        this.errorDTO = errorDTO;
    }

    public DataServiceException(IErrorCode iErrorCode) {
        super(iErrorCode.getError());
        this.timestamp = Instant.now().toEpochMilli();
        this.iErrorCode = iErrorCode;
    }

    public DataServiceException(String message, IErrorCode iErrorCode) {
        super(message);
        this.timestamp = Instant.now().toEpochMilli();
        this.iErrorCode = iErrorCode;
    }

    public DataServiceException(Throwable cause, IErrorCode iErrorCode) {
        super(cause);
        this.timestamp = Instant.now().toEpochMilli();
        this.iErrorCode = iErrorCode;
    }

    public DataServiceException(String message, Throwable cause, IErrorCode iErrorCode) {
        super(message, cause);
        this.timestamp = Instant.now().toEpochMilli();
        this.iErrorCode = iErrorCode;
    }

    public IErrorCode getIErrorCode() {
        return this.iErrorCode;
    }

    public Long getTimestamp(){
        return this.timestamp;
    }

    public DataServiceException setiErrorCode(IErrorCode iErrorCode) {
        this.iErrorCode = iErrorCode;
        return this;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public Map<String, Object> getPrivateMeta() {
        return privateMeta;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }

    public void setErrorDTO(ErrorDTO errorDTO) {
        this.errorDTO = errorDTO;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) meta.get(name);
    }

    public DataServiceException set(String name, Object value) {
        meta.put(name, value);
        return this;
    }

    public DataServiceException setPrivate(String name, Object value) {
        privateMeta.put(name, value);
        return this;
    }
}
