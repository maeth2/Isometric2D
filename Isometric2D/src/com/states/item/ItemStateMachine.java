package com.states.item;

import com.entities.items.Item;
import com.states.StateMachine;

public class ItemStateMachine extends StateMachine<ItemStateMachine.itemStates>{
	
	protected ItemContext context;
	private ItemState use;
	
	public static enum itemStates {
		Picked,
		Idle,
		Use
	}
	
	public ItemStateMachine(Item weapon, ItemState use) {
		this.context = new ItemContext(weapon);
		this.use = use;
		initialiseStates();
		this.currentState = states.get(itemStates.Idle);
	}

	@Override
	public void initialiseStates() {
		states.put(itemStates.Idle, new ItemIdleState(context, itemStates.Idle));
		states.put(itemStates.Picked, new ItemPickedState(context, itemStates.Picked));
		states.put(itemStates.Use, use != null ? use.create(context, itemStates.Use) : null);
	}
}
