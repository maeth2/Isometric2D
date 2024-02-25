package com.entities.items;

import com.GameObject;
import com.entities.Entity;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;
import com.utils.Transform;

public abstract class Item extends GameObject{
	protected Entity entity;
	
	public Item(String name, Transform transform, ItemState use) {
		super(name, transform);
		if(transform != null) {
			addComponents();
		}
		addStateMachine(new ItemStateMachine(this, use));
	}
	
	public abstract void addComponents();
	
	public Entity getEntity() {
		return this.entity;
	}
	
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public abstract Item create(String name, Transform transform);
}
