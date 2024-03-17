package com.states.ai;

public class TestChaseState extends TestState {
	public TestChaseState(TestAI.state stateKey, TestContext context) {
		super(stateKey, context);
	}

	@Override
	public void enter() {
		context.getTarget().setBaseSpeed(300f);
	}

	@Override
	public void exit() {		
	}

	@Override
	public void update(float dt) {
		this.travelDirectToTarget(context.getTargetEntity().transform.position.x, context.getTargetEntity().transform.position.y, dt);
	}

}
