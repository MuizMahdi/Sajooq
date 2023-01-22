package constants;

public enum Operations {
    GENERATE("generate"),
    UPDATE("update");

    private final String operation;

    Operations(final String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return operation;
    }
}
