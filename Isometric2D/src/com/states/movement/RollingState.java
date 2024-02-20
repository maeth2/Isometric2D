package com.states.movement;

import com.Main;
import com.components.AnimationComponent;
import com.utils.AnimationFrames;

public class RollingState extends MovementState {
	
	private float totalFrames = 5;
	private float distance = 250f;
	private float elapsed;
	private float movementDuration;
	private AnimationFrames frames;
	
	private float dx, dy;

	public RollingState(MovementContext context, MovementStateMachine.movementStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		AnimationComponent a = context.getEntity().getComponent(AnimationComponent.class);
		if(a != null) {
			frames = a.animations.get("Rolling");
			a.setCurrentAnimation("Rolling");
			a.setAnimationDuration(frames.getAnimationDuration());
			movementDuration = frames.getAnimationDuration() * ((frames.getActionEndFrame() - frames.getActionStartFrame() + 1.0f) / totalFrames);
		}
		
		
		elapsed = 0;
		dx = 0;
		dy = 0;
		int direction = 0;
		
		if(context.getEntity().getActions("Up")){
			dy = 1;
		}else if(context.getEntity().getActions("Down")) {
			dy = -1;
		}
		
		if(context.getEntity().getActions("Left")){
			direction = 1;
			dx = -1;
		}else if(context.getEntity().getActions("Right")) {
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
		int frame = (int)(totalFrames / frames.getAnimationDuration() * elapsed);
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
			context.getEntity().setDirty(true);
			Main.getScene().updateGrid(context.getEntity());
		}
	}
	
	@Override
	public MovementStateMachine.movementStates next() {
		return nextState;
	}
}
