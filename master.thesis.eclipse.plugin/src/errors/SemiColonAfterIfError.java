package errors;

public class SemiColonAfterIfError extends BaseError {

    public SemiColonAfterIfError(int offset, int length) {
        super(offset, length);
    }

    public String getSuggestion() {
        return "You should try to remove the semicolon (;).";
    }

    @Override
    public boolean hasSuggestion() {
        return true;
    }

    @Override
    public String getWhat() {
        return "You have a semicolon (;) after an if-statement.";
    }

}