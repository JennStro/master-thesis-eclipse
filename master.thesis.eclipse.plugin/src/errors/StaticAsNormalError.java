package errors;

public class StaticAsNormalError extends BaseError {

    private static final ErrorType errorType = ErrorType.STATIC_AS_NORMAL;
    private String containingClass;
    private String methodCall;

    public StaticAsNormalError(int offset, int length) {
        super(offset, length);
    }

    @Override
    public String getSuggestion() {
        return "You should try to make the method non-static or call " + containingClass + "." + methodCall;
    }

    public void setContainingClass(String containingClass) {
        this.containingClass = containingClass;
    }

    public void setMethodCall(String methodCall) {
        this.methodCall = methodCall;
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getExampleOnHowToNotDoIt() {
        return ErrorInformation.getExampleOfHowToNotDoIt(errorType);
    }

    @Override
    public boolean hasSuggestion() {
        return this.containingClass != null && this.methodCall != null;
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
