package application.context.state;

public class Step {

	private String stepText;
	private int ident;
	
	public Step(String stepText, int ident) {
		super();
		this.stepText = stepText;
		this.ident = ident;
	}

	public String getStepText() {
		return stepText;
	}

	public int getIdent() {
		return ident;
	}	

	@Override
	public String toString() {
		return "[" + ident + "] " + stepText;
	}
}
