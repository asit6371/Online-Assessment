package com.onlineassessment.exception;

import com.onlineassessment.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            UserNotFoundException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleUserNotFound(
            UserNotFoundException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(
            QuestionNotFoundException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleQuestionNotFound(
            QuestionNotFoundException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(
            TestNotFoundException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleTestNotFound(
            TestNotFoundException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(
            TestExpiredException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleTestExpired(
            TestExpiredException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
            InvalidCodeException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleInvalidCode(
            InvalidCodeException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
            UnauthorizedSubmissionException.class
    )
    public ResponseEntity<ErrorResponseDto>
    handleUnauthorizedSubmission(
            UnauthorizedSubmissionException ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto>
    handleGenericException(
            Exception ex
    ) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ErrorResponseDto>
    buildErrorResponse(
            String message,
            HttpStatus status
    ) {

        ErrorResponseDto error =
                new ErrorResponseDto();

        error.setMessage(message);

        error.setStatus(
                status.value()
        );

        error.setTime(
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                error,
                status
        );
    }
}