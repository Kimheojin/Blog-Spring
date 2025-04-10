package HeoJin.demoBlog.exception;

public class MemberNotFound extends CustomException{

    private static final String MESSAGE = "가입 목록에 존재 X";

    public MemberNotFound() {
        super(MESSAGE);
    }
    @Override
    public int getstatusCode() {
        return 404;
    }
    //
}
