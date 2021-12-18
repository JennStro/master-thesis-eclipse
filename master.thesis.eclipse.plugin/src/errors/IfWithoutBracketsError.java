package errors;

public class IfWithoutBracketsError extends BaseError {

	private String ifStatement;
	private Object thenBranch;

	public IfWithoutBracketsError(int offset, int length) {
		super(offset, length);
	}
	
	public void setIfStatementWithoutBody(String ifStatement) {
		this.ifStatement = "if ("+ifStatement + ")";
	}
	
	public void setThenBranch(String thenBranch) {
		this.thenBranch = thenBranch;
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
		return this.ifStatement != null && this.thenBranch != null; }

	@Override
	public String getSuggestion() {
		return "You should enclose the body in brackets: \n"
				+ this.ifStatement + " { \n"
				+ "    " +  this.thenBranch + "\n"
				+ "}"
				;
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
