package com.states.ai;

import com.states.State;

public abstract class EnemyState extends State<EnemyStateMachine.state> {
	protected EnemyContext context;
	
	public EnemyState(EnemyStateMachine.state stateKey, EnemyContext context) {
		super(stateKey);
		this.context = context;
	}
	
	public boolean travelToTarget(float x, float y, float dt) {
		context.getTarget().setTargetDestination(x, y);
		float x1 = context.getTarget().transform.position.x;
		float y1 = context.getTarget().transform.position.y;

		boolean fx = Math.abs(x1 - x) >= context.getTarget().getSpeed() * dt;
		boolean fy = Math.abs(y1 - y) >= context.getTarget().getSpeed() * dt;
		
		boolean left = x1 > x && fx;
		boolean right = x1 < x && fx;
		boolean up = y1 < y && fy;
		boolean down = y1 > y && fy;
				
		context.getTarget().setTrigger("Up", up);
		context.getTarget().setTrigger("Down", down);
		context.getTarget().setTrigger("Left", left);
		context.getTarget().setTrigger("Right", right);
		
		if(!fx) {
			context.getTarget().setX(x);
		}
		
		if(!fy) {
			context.getTarget().setY(y);
		}
		
		return !(left || right || up || down);
	}
	
	public void stop() {
		context.getTarget().setTrigger("Up", false);
		context.getTarget().setTrigger("Down", false);
		context.getTarget().setTrigger("Left", false);
		context.getTarget().setTrigger("Right", false);
	}
}
