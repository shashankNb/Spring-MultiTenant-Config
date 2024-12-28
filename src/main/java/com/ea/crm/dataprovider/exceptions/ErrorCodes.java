package com.ea.crm.dataprovider.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Shashank Bhattarai in 12/17/2022
 */
public enum ErrorCodes implements IErrorCode {
    INVALID_JSON_BODY(4003, "Unexpected Json Body",HttpStatus.BAD_REQUEST),
    INVALID_PAYLOAD(4011, "", HttpStatus.BAD_REQUEST),
    INVALID_USERID(4014, "User id is Invalid", HttpStatus.BAD_REQUEST),
    NOT_SUPPORTED(4020, "Not Supported", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4021, "Invalid Token", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(4302, "Not Authorized to access data", HttpStatus.FORBIDDEN),
    INVALID_CREDENTIALS(4303, "Invalid Credentials", HttpStatus.INTERNAL_SERVER_ERROR),

    INTERNAL_SERVER_ERROR(50001, "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND(4041, "Not found", HttpStatus.NOT_FOUND);

    private final int number;
    private final String error;
    private final HttpStatus status;

    ErrorCodes(int number, String error, HttpStatus status) {
        this.number = number;
        this.error = error;
        this.status = status;
    }

    public static ErrorCodes valueOf(int code) {
        for (ErrorCodes errcode : ErrorCodes.values()) {
            if (errcode.ordinal() == code) {
                return errcode;
            }
        }
        return null;
    }

    public static ErrorCodes getErrorCode(int code) {
        for (ErrorCodes errcode : ErrorCodes.values()) {
            if (errcode.number == code) {
                return errcode;
            }
        }
        return null;
    }

    @Override
    public int getCode() {
        return this.number;
    }

    @Override
    public String getError() {
        return this.error;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

}
