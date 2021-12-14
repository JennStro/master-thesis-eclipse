package errors;

public class SemiColonAfterIfError extends BaseError {

    private static final ErrorType errorType = ErrorType.SEMICOLON_AFTER_IF;

    public SemiColonAfterIfError(int offset, int length) {
        super(offset, length);
    }

    public String getSuggestion() {
        return "You should try to remove the semicolon (;).";
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
        return true;
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