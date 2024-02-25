package com.entities.items.weapons.specialstick;

import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;

public class SpecialStickAttackState extends ItemState{	
	private float cooldown = 1f;
	
	public SpecialStickAttackState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		System.out.println("Slashing");
		this.nextState = stateKey;
		this.cooldown = 1f;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		pointToMouse();
		stickToEntity();
		cooldown -= dt;
		if(cooldown <= 0) {
			this.nextState = ItemStateMachine.itemStates.Picked;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new SpecialStickAttackState(context, stateKey);
	}
}
