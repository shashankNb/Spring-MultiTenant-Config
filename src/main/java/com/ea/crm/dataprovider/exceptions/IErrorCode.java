package com.ea.crm.dataprovider.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Shashank Bhattarai on 12/17/2022.
 */
public interface IErrorCode {
    int getCode();
    String getError();
    HttpStatus getStatus();
}
