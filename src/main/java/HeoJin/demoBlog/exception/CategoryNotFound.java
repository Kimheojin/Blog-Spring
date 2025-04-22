package HeoJin.demoBlog.exception;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryNotFound extends CustomException{

    // 404
    private final static String MESSAGE = "존재하지 않는 카테고리 입니다";

    public CategoryNotFound() {
        super(MESSAGE);
    }
    @Override
    public int getstatusCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
