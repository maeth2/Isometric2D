package com.entities.items.weapons.syringe;

import com.components.AnimationComponent;
import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;

public class SyringeAttackState extends ItemState{	
	private float cooldown;
	
	public SyringeAttackState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		this.nextState = stateKey;
		AnimationComponent a = context.getItem().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Attack");
		}
		cooldown = 1f;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		cooldown -= dt;
		pointToMouse();
		stickToEntity();
		if(cooldown <= 0) {
			this.nextState = ItemStateMachine.itemStates.Picked;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new SyringeAttackState(context, stateKey);
	}
}
