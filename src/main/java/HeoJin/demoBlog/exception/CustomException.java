package HeoJin.demoBlog.exception;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomException extends RuntimeException{
/*
* {
*       statuscode :
*       message :
*          [
* validation : ~~~ -> 이런 구조 생각
* ]
*
*
* }

* */

    public final Map<String, String> validation = new HashMap<>();

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {// cause -> 부모 exception
        super(message, cause);
    }

    public abstract int getstatusCode();

    public void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }


}
