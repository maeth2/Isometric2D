package com.states.item;

import com.components.AABBComponent;
import com.entities.items.Item;

public class ItemPickedState extends ItemState {	
	
	public ItemPickedState(ItemContext context, ItemStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		context.getTarget().setPivotY(-0.4f);
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Item.states.Picked);
		}
		context.getTarget().setLayer(2);
		context.getTarget().getComponent(AABBComponent.class).setCollision(false);
	}

	@Override
	public void exit() {		
	}

	@Override
	public void update(float dt) {
		pointToDestination();
		stickToEntity();
		
		if(context.getTarget().getEntity().getTrigger("Drop")) {
			nextState = ItemStateMachine.state.Idle;
			context.getTarget().getEntity().getInventory().setSelected(null);
			context.getTarget().setEntity(null);
		}else if(context.getTarget().getEntity().getTrigger("L_Click")) {
			nextState = ItemStateMachine.state.Use;
		}
	}
}
