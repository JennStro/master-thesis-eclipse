package errors;

public class BitwiseOperatorError extends BaseError {

    private static final ErrorType errorType = ErrorType.BITWISE_OPERATOR;
    private String leftOperand;
    private String operator;
    private String rightOperand;

    public BitwiseOperatorError(int offset, int length) {
        super(offset, length);
    }

    @Override
    public String getSuggestion() {
        return "You should try " + this.leftOperand + " " + convertBitwiseOperatorToConditionalOperator(this.operator) + " " + this.rightOperand;
    }

    public void setLeftOperand(String leftOperand) {
        this.leftOperand = leftOperand;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setRightOperand(String rightOperand) {
        this.rightOperand = rightOperand;
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
        return this.leftOperand != null && this.operator != null && this.rightOperand != null;
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

    private String convertBitwiseOperatorToConditionalOperator(String operator) {
        if ("&".equals(operator)) {
            return "&&";
        }
        if ("|".equals(operator)) {
            return "||";
        }
        return "";
    }
}
