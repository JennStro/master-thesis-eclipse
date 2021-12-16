package errors;

public class FieldDeclarationWithoutInitializerError extends BaseError {

	public FieldDeclarationWithoutInitializerError(int offset, int length) {
		super(offset, length);
	}

	@Override
	public ErrorType getErrorType() {
		return ErrorType.FIELD_DECL_NO_INIT;
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
		return "You should initialize the field variable!";
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
