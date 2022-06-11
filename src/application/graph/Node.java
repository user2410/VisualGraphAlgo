package application.graph;

public class Node {

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

}
