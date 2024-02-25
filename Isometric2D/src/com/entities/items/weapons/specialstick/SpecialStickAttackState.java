package com.entities.items.weapons.specialstick;

import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;

public class SpecialStickAttackState extends ItemState{	
	private float elapsed = 0f;
	private float cooldown = 0.5f;
	
	public SpecialStickAttackState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation("Attack");
		}
		this.nextState = stateKey;
		this.elapsed = 0f;
		
		context.getItem().transform.scale.x *= 2;
		context.getItem().transform.scale.y *= 2;
		context.getItem().transform.pivot.y = 0f;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		stickToEntity();
		float angle = (float)Math.toRadians(context.getItem().transform.rotation.x) + 90f;
		float dx = (float) Math.cos(angle) * Math.abs(context.getItem().transform.scale.x) * 0.2f;
		float dy = (float) Math.sin(angle) * context.getItem().transform.scale.y * 0.2f;
		context.getItem().transform.position.x += dx;
		context.getItem().transform.position.y += dy;
		elapsed += dt;
		if(elapsed >= cooldown) {
			this.nextState = ItemStateMachine.itemStates.Picked;
		}
		if(context.getAnimation().getCurrentFrame() == -1) {
			context.getItem().transform.scale.x /= 2;
			context.getItem().transform.scale.y /= 2;
			context.getAnimation().setCurrentAnimation("Picked");
		}
		context.getItem().setDirty(true);
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new SpecialStickAttackState(context, stateKey);
	}
}
