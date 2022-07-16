package application.context.state;

public class Step {

	private int stepnum;
	private String stepText;
	private int ident;
	
	public Step(String stepText, int ident, int stepnum) {
		super();
		this.stepText = stepText;
		this.ident = ident;
		this.stepnum = stepnum;
	}

	public String getStepText() {
		return stepText;
	}

	public int getIdent() {
		return ident;
	}	

	public int getStepnum() {
		return stepnum;
	}

	@Override
	public String toString() {
		return "[" + ident + "] " + stepText;
	}
}
