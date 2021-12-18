package errors;

public class FieldDeclarationWithoutInitializerError extends BaseError {

	private String type;
	private String className;
	private String name;

	public FieldDeclarationWithoutInitializerError(int offset, int length) {
		super(offset, length);
	}
	
	public void setFieldVariableType(String type) {
		this.type = type;
	}
	
	public void setClass(String className) {
		this.className = className;
	}
	
	public void setFieldVariableName(String name) {
		this.name = name;
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
		return this.className != null &&
				this.name != null &&
				this.type != null;
	}

	@Override
	public String getSuggestion() {
		return "You could initialize the fieldvariable in the constructor: \n \n"
				+ "public " + this.className + "(" + this.type + " " + this.name + ") { \n "
						+ "	this." + this.name + " = " + this.name + "\n"
						+ "}";
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
