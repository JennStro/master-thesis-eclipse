package errors;


public class IgnoringReturnError extends BaseError {

    private static final ErrorType errorType = ErrorType.IGNORING_RETURN_VALUE;
    private String returnType;
    private String methodCall;

    public IgnoringReturnError(int offset, int length) {
        super(offset, length);
    }
    @Override
    public String getSuggestion() {
        return "You should try " + this.returnType + " variableName = " + this.methodCall + ";";
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getExampleOnHowToNotDoIt() {
        return ErrorInformation.getExampleOfHowToNotDoIt(errorType);
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setMethodCall(String methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public boolean hasSuggestion() {
        return this.returnType != null && this.methodCall != null;
    }

    @Override
    public String getExampleOnHowToDoIt() {
        return ErrorInformation.getExampleOfHowToDoIt(errorType);
    }

    @Override
    public String getWhat() {
        return ErrorInformation.getDescriptionOf(errorType);
    }

    @Override
    public String getWhy() {
        return ErrorInformation.getCauseOf(errorType);
    }

    @Override
    public String getLongExplanation() {
        return null;
    }
}