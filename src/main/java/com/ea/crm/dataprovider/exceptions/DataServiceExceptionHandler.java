package com.ea.crm.dataprovider.exceptions;


import com.ea.crm.dataprovider.dto.ErrorDTO;
import com.ea.crm.dataprovider.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * @author By Shashank Bhattarai on 12/17/2022
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class DataServiceExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataServiceExceptionHandler.class);
    private static final String ERROR_ID_DETAIL_LOG_MSG = "ErrorId: {}, Details: {}";

    @ExceptionHandler(DataServiceException.class)
    protected ResponseEntity<Object> handleDataServiceException(DataServiceException ex) {
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.putAll(ex.getMeta());
        metaMap.putAll(ex.getPrivateMeta());
        String metaString = !metaMap.isEmpty() ? "Details: " + getStringForMap(metaMap) : "";
        logger.error("ErrorId: {}, {}.", ex.getTimestamp(), metaString, ex);

        if (null == ex.getErrorDTO()) {
            ErrorDTO apiError = new ErrorDTO();
            apiError.setId(ex.getTimestamp());
            apiError.setStatus(ex.getIErrorCode().getStatus().value());
            apiError.setCode(ex.getIErrorCode().getCode());
            apiError.setTitle(ex.getIErrorCode().getError());
            apiError.setMeta(ex.getMeta());
            addErrorMessageAndStackTrace(apiError, ex);
            return buildResponseEntity(apiError, ex.getIErrorCode().getStatus());
        } else {
            addErrorMessageAndStackTrace(ex.getErrorDTO(), ex);
            // logErrorToDatabase(ex.getErrorDTO());
            return buildResponseEntity(ex.getErrorDTO(), HttpStatus.valueOf(ex.getErrorDTO().getStatus()));
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ErrorDTO apiError = new ErrorDTO();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("URL", ex.getRequestURL());
        logger.error(ERROR_ID_DETAIL_LOG_MSG, apiError.getId(), getStringForMap(metaMap), ex);
        apiError.setStatus(HttpStatus.NOT_FOUND.value());
        apiError.setTitle("No handler found for your request");
        addErrorMessageAndStackTrace(apiError, ex);
        apiError.setMeta(metaMap);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex,
                                                                              WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("Method", ex.getMethod());
        metaMap.put("SupportedMethod", ex.getSupportedMethods());
        logger.error(ERROR_ID_DETAIL_LOG_MSG, apiError.getId(), getStringForMap(metaMap), ex);
        apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        apiError.setTitle("Request Method is not supported");
        addErrorMessageAndStackTrace(apiError, ex);
        apiError.setMeta(metaMap);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(GenericJDBCException.class)
    protected ResponseEntity<Object> handleGenericJDBCException(GenericJDBCException ex,
                                                                WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("sql", ex.getSQL());
        metaMap.put("SupportedMethod", ex.getMessage());
        logger.error(ERROR_ID_DETAIL_LOG_MSG, apiError.getId(), getStringForMap(metaMap), ex);
        apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        apiError.setTitle("Request Method is not supported");
        addErrorMessageAndStackTrace(apiError, ex);
        apiError.setMeta(metaMap);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleRequestParameterMissingException(MissingServletRequestParameterException ex,
                                                                            WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("ParameterName", ex.getParameterName());
        metaMap.put("ParameterType", ex.getParameterType());
        logger.error(ERROR_ID_DETAIL_LOG_MSG, apiError.getId(), getStringForMap(metaMap), ex);
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setTitle("Request Parameter is Missing");
        addErrorMessageAndStackTrace(apiError, ex);
        apiError.setMeta(metaMap);
        // errorLogService.logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<Object> handleMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex,
                                                                          WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("ContentType", ex.getContentType());
        metaMap.put("SupportedTypes", ex.getSupportedMediaTypes());
        logger.error(ERROR_ID_DETAIL_LOG_MSG, apiError.getId(), getStringForMap(metaMap), ex);
        apiError.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
        apiError.setTitle("Media Type Not Supported");
        addErrorMessageAndStackTrace(apiError, ex);
        apiError.setMeta(metaMap);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                           WebRequest webRequest) {
        Map<String, Map<String, ArrayList<String>>> validation_errors = new HashMap<>();
        Map<String, ArrayList<String>> data = new HashMap<>();
        Map<String, Object> metaMap = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            data.put(error.getField(), new ArrayList<>(List.of(error.getDefaultMessage())));
            metaMap.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            data.put(error.getObjectName(), new ArrayList<>(List.of(error.getDefaultMessage())));
            metaMap.put(error.getObjectName(), error.getDefaultMessage());
        }
        validation_errors.put("validation_error", data);

        // logErrorToDatabase(apiError);
        return new ResponseEntity<>(validation_errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleInvalidFormatException(HttpMessageNotReadableException ex,
                                                                  WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        logger.error("ErrorId: {}", apiError.getId(), ex);
        apiError.setStatus(HttpStatus.BAD_REQUEST.value());
        apiError.setTitle("Message Not Readable");
        addErrorMessageAndStackTrace(apiError, ex);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    protected ResponseEntity<Object> handleBadSqlGrammarException(
            BadSqlGrammarException ex,
            WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        logger.error("ErrorId: {}", apiError.getId(), ex);
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setTitle("Bad SQL Grammer Exception");
        addErrorMessageAndStackTrace(apiError, ex);
        // logErrorToDatabase(apiError);
        apiError.setDetail("Bad SQL Grammer Exception");
        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(BadSqlGrammarException ex, WebRequest webRequest) {
        ErrorDTO apiError = new ErrorDTO();
        logger.error("ErrorId: {}", apiError.getId(), ex);
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setTitle("Bad SQL Grammar Exception");
        addErrorMessageAndStackTrace(apiError, ex);
        // logErrorToDatabase(apiError);
        apiError.setDetail("Bad SQL Grammar Exception");
        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleAllOtherException(Throwable ex) {
        ErrorDTO apiError = new ErrorDTO();
        logger.error("ErrorId: {}", apiError.getId(), ex);
        apiError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiError.setTitle("Internal Server Error");
        addErrorMessageAndStackTrace(apiError, ex);
        // logErrorToDatabase(apiError);
        return buildResponseEntity(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addErrorMessageAndStackTrace(ErrorDTO apiError, Throwable ex) {
        apiError.setDetail(ex.getMessage());
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        apiError.setStackTrace(stringWriter.toString());
        apiError.setId(0L);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorDTO apiError, HttpStatus status) {
        apiError.setStackTrace(null);
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setErrors(Collections.singletonList(apiError));
        return new ResponseEntity<>(responseDTO, status);
    }

    private String getStringForMap(Map<String, Object> map) {
        String mapString = "";
        try {
            mapString = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            logger.error("Cannot process map to string", e);
        }
        return mapString;
    }


}
