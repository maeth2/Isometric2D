package com.states.ai;

import com.states.State;

public abstract class TestState extends State<TestAI.state> {
	protected TestContext context;
	
	public TestState(TestAI.state stateKey, TestContext context) {
		super(stateKey);
		this.context = context;
	}
	
	public boolean travelDirectToTarget(float x, float y, float dt) {
		context.getTarget().setTargetDestination(x, y);
		float x1 = context.getTarget().transform.position.x;
		float y1 = context.getTarget().transform.position.y;

		boolean fx = Math.abs(x1 - x) > context.getTarget().getSpeed() * dt;
		boolean fy = Math.abs(y1 - y) > context.getTarget().getSpeed() * dt;
		
		boolean left = x1 > x && fx;
		boolean right = x1 < x && fx;
		boolean up = y1 < y && fy;
		boolean down = y1 > y && fy;
		
		context.getTarget().setTrigger("Up", up);
		context.getTarget().setTrigger("Down", down);
		context.getTarget().setTrigger("Left", left);
		context.getTarget().setTrigger("Right", right);
		
		return !(left || right || up || down);
	}
}
