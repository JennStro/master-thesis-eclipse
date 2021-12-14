package errors;

public class MissingEqualsMethodError extends BaseError {

	public MissingEqualsMethodError(int offset, int length) {
		super(offset, length);
	}

	@Override
	public ErrorType getErrorType() {
		// TODO Auto-generated method stub
		return null;
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
		return "You should implement the equals method!";
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
