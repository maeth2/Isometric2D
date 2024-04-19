package com.states.movement;

import com.Main;
import com.entities.Entity;

public class RollingState extends MovementState {
	
	private float distance = 250f;
	private float elapsed;
	private float movementDuration;
	
	private float dx, dy;

	public RollingState(MovementContext context, MovementStateMachine.state stateKey) {
		super(context, stateKey);
		this.cooldown = 0.5f;
	}

	@Override
	public void enter() {
		entryTime = Main.getTimeElapsed();
		if(context.getAnimation() != null) {
			frames = context.getAnimation().setCurrentAnimation(Entity.states.Rolling);
			movementDuration = frames.getAnimationDuration() * ((frames.getActionEndFrame() - frames.getActionStartFrame() + 1.0f) / frames.getTotalFrames());
		}
		
		elapsed = 0;
		dx = 0;
		dy = 0;
		int direction = 0;
		
		if(context.getTarget().getTrigger("Up")){
			dy = 1;
		}else if(context.getTarget().getTrigger("Down")) {
			dy = -1;
		}
		
		if(context.getTarget().getTrigger("Left")){
			direction = 1;
			dx = -1;
		}else if(context.getTarget().getTrigger("Right")) {
			direction = -1;
			dx = 1;
		}
		
		if(dx == 0 && dy == 0) {
			nextState = MovementStateMachine.state.Idle;
			return;
		}
				
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		dx = dx / length * distance;
		dy = dy / length * distance;
		
		if(direction != 0) {
			context.getTarget().setScaleX(Math.abs(context.getTarget().getTransform().getScale().x) * direction);
		}
	}

	@Override
	public void exit() {}

	@Override
	public void update(float dt) {
		elapsed += dt;
		if(elapsed >= frames.getAnimationDuration()) {
			nextState = MovementStateMachine.state.Idle;
			return;
		}
		if(context.getAnimation().getCurrentAnimation().inActionFrame(context.getAnimation().getCurrentFrame())){
			velocity.x = dx / movementDuration * dt;
			velocity.y = dy / movementDuration * dt;
			
			checkCollision();
			
			if(velocity.y == 0 && velocity.x != 0) {
				dx = dx / Math.abs(dx) * distance;
			}else if(velocity.y != 0 && velocity.x == 0){
				dy = dy / Math.abs(dy) * distance;
			}
			
			velocity.x = dx / movementDuration * dt;
			velocity.y = dy / movementDuration * dt;
			
			checkCollision();
			
			context.getTarget().changePosition(velocity.x, velocity.y);
		}
	}
}