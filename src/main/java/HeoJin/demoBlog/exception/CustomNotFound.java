package HeoJin.demoBlog.exception;

import jakarta.servlet.http.HttpServletResponse;

public class CustomNotFound extends CustomException{

    private final static String MESSAGE = "존재하지 않는 entity 입니다. : ";

    public CustomNotFound(String entity) {
        super(MESSAGE + entity);
    }

    @Override
    public int getstatusCode() {
        return HttpServletResponse.SC_NOT_FOUND; // 404
    }
}
