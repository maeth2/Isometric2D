package com.states.movement;

import com.Main;

public class RollingState extends MovementState {
	
	private float distance = 250f;
	private float elapsed;
	private float movementDuration;
	
	private float dx, dy;

	public RollingState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		if(context.getAnimation() != null) {
			frames = context.getAnimation().setCurrentAnimation("Rolling");
			movementDuration = frames.getAnimationDuration() * ((frames.getActionEndFrame() - frames.getActionStartFrame() + 1.0f) / frames.getTotalFrames());
		}
		
		
		elapsed = 0;
		dx = 0;
		dy = 0;
		int direction = 0;
		
		if(context.getEntity().getTrigger("Up")){
			dy = 1;
		}else if(context.getEntity().getTrigger("Down")) {
			dy = -1;
		}
		
		if(context.getEntity().getTrigger("Left")){
			direction = 1;
			dx = -1;
		}else if(context.getEntity().getTrigger("Right")) {
			direction = -1;
			dx = 1;
		}
		
		if(dx == 0 && dy == 0) {
			nextState = MovementStateMachine.movementStates.Idle;
			return;
		}
				
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / length * distance;
		dy = dy / length * distance;
		
		if(direction != 0) context.getEntity().transform.scale.x = Math.abs(context.getEntity().transform.scale.x) * direction;
		
		nextState = stateKey;
	}

	@Override
	public void exit() {}

	@Override
	public void update(float dt) {
		int frame = (int)(frames.getTotalFrames() / frames.getAnimationDuration() * elapsed);
		elapsed += dt;
		if(elapsed >= frames.getAnimationDuration()) {
			nextState = MovementStateMachine.movementStates.Idle;
			return;
		}
		if(frame >= frames.getActionStartFrame() && frame <= frames.getActionEndFrame()){
			velocity.x = dx / movementDuration * dt;
			velocity.y = dy / movementDuration * dt;
			
			checkCollision();

			context.getEntity().transform.position.x += velocity.x;
			context.getEntity().transform.position.y += velocity.y;
			
			Main.getScene().updateGrid(context.getEntity());
			context.getEntity().setDirty(true);
		}
	}
}
