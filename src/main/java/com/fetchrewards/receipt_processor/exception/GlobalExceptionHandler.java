package com.fetchrewards.receipt_processor.exception;

import com.fetchrewards.receipt_processor.model.ErrorResponse;
import com.fetchrewards.receipt_processor.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors triggered by @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ErrorResponse(Strings.INVALID_RECEIPT), HttpStatus.BAD_REQUEST);
    }

    // Handle not found errors (when receipt is not found)
    @ExceptionHandler(ReceiptNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(ReceiptNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(Strings.RECEIPT_NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    // Handle DateTimeParseException (and similar parsing errors)
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(Strings.INVALID_RECEIPT));
    }
}
