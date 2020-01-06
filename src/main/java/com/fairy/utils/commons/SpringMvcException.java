package com.fairy.utils.commons;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class SpringMvcException extends ResponseEntityExceptionHandler  {


    @ExceptionHandler(DisabledException.class)//可以直接写@EceptionHandler，IOExeption继承于Exception
    public Object allExceptionHandler(Exception exception){

        return "1";
    }



    /**
     * Handle MissingServletRequestParameterException. Triggered when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the apiEx object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
       
        return buildResponseEntity(new ApiException(BAD_REQUEST, error,getRequestUrl(request), ex));
    }
    
    @Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
			NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = request.getContextPath() + " url is missing";
		
		
        return buildResponseEntity(new ApiException(NOT_FOUND, error,getRequestUrl(request), ex));
	}
    
    
    @Override
   	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
   			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
   		String error = request.getContextPath() + " MethodNotSupported";
           return buildResponseEntity(new ApiException(HttpStatus.METHOD_NOT_ALLOWED, error,getRequestUrl(request),ex));
   	}
    


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the apiEx object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        return buildResponseEntity(new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2), getRequestUrl(request),ex));
    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the apiEx object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
    	ApiException apiEx = new ApiException(BAD_REQUEST);
        apiEx.setMessage("Validation error");
        apiEx.setUrl(getRequestUrl(request));
        apiEx.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiEx.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiEx);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the apiEx object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
    	ApiException apiEx = new ApiException(BAD_REQUEST);
        apiEx.setMessage("Validation error");
        apiEx.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiEx);
    }

    

    /**
     * Handle HttpMessageNotReadableException. Happens when request JSON is malformed.
     *
     * @param ex      HttpMessageNotReadableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the apiEx object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiException(HttpStatus.BAD_REQUEST, error, getRequestUrl(request),ex));
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the apiEx object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        return buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, error,getRequestUrl(request), ex));
    }

    /**
     * Handle javax.persistence.EntityNotFoundException
     */
//    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
//    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
//        return buildResponseEntity(new ApiException(HttpStatus.NOT_FOUND, ex));
//    }

    /**
     * Handle DataIntegrityViolationException, inspects the cause for different DB causes.
     *
     * @param ex the DataIntegrityViolationException
     * @return the apiEx object
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiException(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        
        ApiException apiEx = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,ex);
        apiEx.setUrl(getRequestUrl(request));
        
        return buildResponseEntity(apiEx);
    }

    /**
     * Handle Exception, handle generic Exception.class
     *
     * @param ex the Exception
     * @return the apiEx object
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
    	ApiException apiEx = new ApiException(BAD_REQUEST);
        apiEx.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        apiEx.setDebugMessage(ex.getMessage());
        apiEx.setUrl(getRequestUrl(request));
        return buildResponseEntity(apiEx);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> unknownException(Exception ex) {
    	
    	//log.error(ex.getStackTrace().toString());
    	//ex.printStackTrace();
    	log.error("Unexpected error",ex);
        return  buildResponseEntity(new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiException apiEx) {
    	
    	ResponseEntity<Object> result = new  ResponseEntity<>(apiEx, apiEx.getStatus());
    	
    	log.error(result.toString());
    	return result;
    }
    
    
    
    public String getRequestUrl(WebRequest request) {
    	
    	String url = null;
    	
    	ServletWebRequest servletWebRequest = (ServletWebRequest) request;
    	
    	url = servletWebRequest.getHttpMethod()+servletWebRequest.getRequest().getServletPath();
    	return url;
    	
    	
    }

	

}