package com.states.item;

import com.entities.items.Item;
import com.states.StateMachine;

public class ItemStateMachine extends StateMachine<ItemStateMachine.state, Item, ItemContext>{
	
	protected Item item;
	
	public static enum state {
		Picked,
		Idle,
		Use
	}
	
	public ItemStateMachine(Item item, ItemState use) {
		super(state.Idle, new ItemContext(item));
		this.item = item;
		addState(state.Use, use != null ? use.create(getContext(), state.Use) : null);
	}

	@Override
	public void initialiseStates() {
		states.put(state.Idle, new ItemIdleState(getContext(), state.Idle));
		states.put(state.Picked, new ItemPickedState(getContext(), state.Picked));
	}
}
