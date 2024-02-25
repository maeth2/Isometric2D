package com.states.item;

import com.entities.items.Item;

public class ItemContext{
	private Item item;
	
	public ItemContext(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}
}
