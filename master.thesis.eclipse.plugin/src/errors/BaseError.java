package errors;

public abstract class BaseError {

    private final int offset;
    private final int length;

    public BaseError(int offset, int length) {
        this.offset = offset;
        this.length = length;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getLength() {
        return this.length;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof BaseError) {
            return this.getErrorType() == ((BaseError) other).getErrorType();
        }
        return false;
    }

    public abstract ErrorType getErrorType();

    /**
     *
     * @return An example of the error
     */
    public abstract String getExampleOnHowToNotDoIt();

    /**
     *
     * @return True if this error has a suggestion available.
     */
    public abstract boolean hasSuggestion();

    /**
     *
     * @return A string with a suggestion on how to fix this error.
     *
     * Check {@link #hasSuggestion()} first.
     */
    public abstract String getSuggestion();

    /**
     *
     * @return An example of the error corrected
     */
    public abstract String getExampleOnHowToDoIt();

    /**
     *
     * @return a string about what caused the error.
     * For example, an EqualsOperatorError is caused by using == instead of .equals on an object.
     */
    public abstract String getWhat();

    /**
     *
     * @return a string describing why the error is a problem.
     * For example, and EqualsOperatorError is a problem because == compares references, not the object.
     */
    public abstract String getWhy();

    public abstract String getLongExplanation();

}