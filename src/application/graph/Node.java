package application.graph;

import java.io.Serializable;

public class Node implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6373045310265172839L;
	
	int id;
	int x, y;

	public Node(int id, int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
