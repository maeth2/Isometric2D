package com.states.item;

import com.components.AnimationComponent;

public class ItemPickedState extends ItemState {	
	
	public ItemPickedState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		this.nextState = stateKey;
		this.context.getItem().transform.pivot.y = -0.4f;
		AnimationComponent a = context.getItem().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Picked");
		}
	}

	@Override
	public void exit() {		
	}

	@Override
	public void update(float dt) {
		pointToMouse();
		stickToEntity();
		
		if(context.getItem().getEntity().getTrigger("Drop")) {
			this.nextState = ItemStateMachine.itemStates.Idle;
			this.context.getItem().setEntity(null);
		}else if(context.getItem().getEntity().getTrigger("L_Click")) {
			this.nextState = ItemStateMachine.itemStates.Use;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new ItemPickedState(context, stateKey);
	}
}
