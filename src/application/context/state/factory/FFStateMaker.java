package application.context.state.factory;

import java.util.ArrayList;
import java.util.Arrays;

import application.context.state.Step;

public class FFStateMaker extends EKStateMaker {

	public static final ArrayList<Step> steps = new ArrayList<Step>(Arrays.asList(
			new Step("initMaxFlow", 0, 0),
			new Step("while there is an augmenting path", 0, 1),
			new Step("find an augmenting path using DFS", 1, 2),
			new Step("for each edge u->v in the path", 1, 3),
			new Step("decrease capacity u->v by bottleneck", 2, 4),
			new Step("increase capacity v->u by bottleneck", 2, 5),
			new Step("increase maxflow by bottleneck", 1, 6)
	));

	@Override
	public ArrayList<Step> getSteps() {
		return steps;
	}

}
