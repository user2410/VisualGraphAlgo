package application.context;

public class ContextController extends Thread{

	private Context context;
	
	public ContextController(Context context) {
		this.context = context;
		setName("Context_controller");
	}	
	
	@Override
	public void run(){
		context.play();
	}
	
}
