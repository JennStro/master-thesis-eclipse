package errors;

public class IfWithoutBracketsError extends BaseError {

	public IfWithoutBracketsError(int offset, int length) {
		super(offset, length);
	}

	@Override
	public ErrorType getErrorType() {
		return ErrorType.IF_WITHOUT_BRACKETS;
	}

	@Override
	public String getExampleOnHowToNotDoIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasSuggestion() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSuggestion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExampleOnHowToDoIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWhat() {
		return "You should use brackets after if!";
	}

	@Override
	public String getWhy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLongExplanation() {
		// TODO Auto-generated method stub
		return null;
	}

}
