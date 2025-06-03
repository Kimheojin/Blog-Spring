package HeoJin.demoBlog.global.exception;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .statusCode(statusCode)
                .code(String.valueOf(statusCode))  // statusCode를 code로 변환
                .build();

        // validation 데이터가 있으면 복사
        if (!e.getValidation().isEmpty()) {
            e.getValidation().forEach(response::addValidation);
        }

        return ResponseEntity.status(statusCode).body(response);
    }
}
