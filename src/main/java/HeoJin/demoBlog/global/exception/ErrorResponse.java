package HeoJin.demoBlog.global.exception;


import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {

    private final int statusCode;
    private final String message;


    @Builder.Default // null 체크 관련
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String field, String errorMessage) {
        this.validation.put(field, errorMessage);
    }
}
