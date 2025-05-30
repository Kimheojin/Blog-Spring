package HeoJin.demoBlog.global.exception;


import jakarta.servlet.http.HttpServletResponse;

public class CategoryAlreadyExist extends CustomException {


    private static final String MESSAGE = "이미 존재하는 카테고리 입니다.";

    public CategoryAlreadyExist() {
        super(MESSAGE);
    }

    @Override
    public int getstatusCode() {
        return HttpServletResponse.SC_BAD_REQUEST;
    } // 400
}
