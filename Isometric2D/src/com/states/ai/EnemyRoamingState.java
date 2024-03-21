package com.states.ai;

import org.joml.Vector2f;

import com.scenes.Scene;

public class EnemyRoamingState extends EnemyState{
	
	private Vector2f point1 = new Vector2f(1.5f * Scene.UNIT_SIZE, 3.5f * Scene.UNIT_SIZE);
	private Vector2f point2 = new Vector2f(7.5f * Scene.UNIT_SIZE, 3.5f * Scene.UNIT_SIZE);
	private Vector2f currentPoint;
	
	public EnemyRoamingState(EnemyStateMachine.state stateKey, EnemyContext context) {
		super(stateKey, context);
	}

	@Override
	public void enter() {
		currentPoint = point1;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		if(this.travelToTarget(currentPoint.x, currentPoint.y, dt)) {
			if(currentPoint == point1) {
				currentPoint = point2;
			}else {
				currentPoint = point1;
			}
		}
		if(context.getTargetEntity() != null) {
			nextState = EnemyStateMachine.state.Chase;
		}
	}
}
