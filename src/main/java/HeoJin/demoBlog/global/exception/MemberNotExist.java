package HeoJin.demoBlog.global.exception;

public class MemberNotExist extends CustomException{

    private static final String MESSAGE = "authenticate 객체에 존재하지 않습니다.";

    public MemberNotExist() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 404;
    }
    //
}
