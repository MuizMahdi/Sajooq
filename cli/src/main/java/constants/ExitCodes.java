package constants;

public enum ExitCodes {
    WITHOUT_ERRORS(0),
    WITH_ERRORS(1);

    private final int code;

    ExitCodes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
