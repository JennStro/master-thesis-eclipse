package errors;

public class EqualsOperatorError extends BaseError {

    private static final ErrorType errorType = ErrorType.NOT_USING_EQUALS;
    private String objectOne;
    private String objectTwo;

    public EqualsOperatorError(int offset, int length) {
        super(offset, length);
    }

    @Override
    public ErrorType getErrorType() {
        return errorType;
    }

    public String getSuggestion() {
        return "You should try " + this.objectOne + ".equals(" + this.objectTwo + ")";
    }

    public void setObjectOne(String objectOne) {
        this.objectOne = objectOne;
    }

    public void setObjectTwo(String objectTwo) {
        this.objectTwo = objectTwo;
    }

    @Override
    public String getExampleOnHowToNotDoIt() {
        return ErrorInformation.getExampleOfHowToNotDoIt(errorType);
    }

    @Override
    public boolean hasSuggestion() {
        return this.objectOne != null && this.objectTwo != null;
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
