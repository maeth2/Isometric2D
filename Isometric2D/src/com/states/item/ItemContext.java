package com.states.item;

import com.components.AnimationComponent;
import com.entities.items.Item;

public class ItemContext{
	private Item item;
	private AnimationComponent animation;
	
	public ItemContext(Item item) {
		this.item = item;
		animation = item.getComponent(AnimationComponent.class);
	}

	public Item getItem() {
		return item;
	}
	
	public AnimationComponent getAnimation() {
		return animation;
	}
}
