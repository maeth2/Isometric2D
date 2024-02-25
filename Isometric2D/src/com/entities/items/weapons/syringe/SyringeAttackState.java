package com.entities.items.weapons.syringe;

import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;

public class SyringeAttackState extends ItemState{	
	private float elapsed;
	private float cooldown = 1f;
	
	public SyringeAttackState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		this.nextState = stateKey;
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation("Attack");
		}
		elapsed = 0f;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		elapsed += dt;
		pointToMouse();
		stickToEntity();
		if(elapsed >= cooldown) {
			this.nextState = ItemStateMachine.itemStates.Picked;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new SyringeAttackState(context, stateKey);
	}
}
