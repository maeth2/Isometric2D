package com.states.item;

import com.components.AnimationComponent;
import com.entities.items.Item;
import com.states.Context;

public class ItemContext extends Context<Item>{
	private AnimationComponent animation;
		
	public ItemContext(Item target) {
		super(target);
		this.animation = target.getComponent(AnimationComponent.class);
	}
	
	public AnimationComponent getAnimation() {
		return animation;
	}
}
