package HeoJin.demoBlog.exception;


import HeoJin.demoBlog.dto.ErrorResponseDto;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException e) {
        int statusCode = e.getstatusCode();

        ErrorResponseDto response = ErrorResponseDto.builder()
                .statusCode(statusCode)
                .message(e.getMessage())
                .build();

        // validation 데이터가 있으면 복사
        if (!e.getValidation().isEmpty()) {
            e.getValidation().forEach(response::addValidation);
        }

        return ResponseEntity.status(statusCode).body(response);
    }
}
