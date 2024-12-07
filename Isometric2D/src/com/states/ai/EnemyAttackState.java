package com.states.ai;

import com.states.ai.EnemyStateMachine.state;
import com.utils.Maths;

public class EnemyAttackState extends EnemyState{

	public EnemyAttackState(state stateKey, EnemyContext context) {
		super(stateKey, context);
	}

	@Override
	public void enter() {}

	@Override
	public void exit() {
		context.getTarget().setTrigger("L_Click", false);
	}

	@Override
	public void update(float dt) {
		float distance = Maths.getEuclideanDistance(
				context.getTarget().getTransform().getPosition().x, 
				context.getTarget().getTransform().getPosition().y, 
				context.getTargetEntity().getTransform().getPosition().x, 
				context.getTargetEntity().getTransform().getPosition().y
		);
		if(distance <= this.context.attackRadius) {
			context.getTarget().setTrigger("L_Click", true);
			stop();
		}else {
			this.nextState = EnemyStateMachine.state.Chase;
		}
	}

}
