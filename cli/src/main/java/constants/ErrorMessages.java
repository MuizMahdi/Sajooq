package constants;

public final class ErrorMessages {
    public static final String INVALID_DB_CREDENTIALS = "Invalid database credentials, please enter database name, user, and password";
    public static final String INVALID_DB_OPERATION = "Invalid operation, only 'update' operation is supported for database";
    public static final String INVALID_ENTITY_OPERATION = "Invalid operation, only 'generate' operation is supported for entity";
    public static final String INVALID_OPERATION = "Invalid operation, only 'generate', and 'update' operations allowed";
    public static final String INVALID_OPERAND = "Invalid operand, only 'changelog', 'entity', and 'database' operands allowed";


    private ErrorMessages() {}
}
