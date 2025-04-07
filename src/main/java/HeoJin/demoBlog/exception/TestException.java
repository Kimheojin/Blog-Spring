package HeoJin.demoBlog.exception;

public class TestException extends CustomException{

    private static final String MESSAGE = "test exception 입니다";

    public TestException() {
        super(MESSAGE);
    }

    @Override
    public int getstatusCode() {
        return 200;
    }
}
