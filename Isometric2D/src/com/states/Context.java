package com.states;

import com.GameObject;

public abstract class Context<T extends GameObject> {
	protected T target;
	
	public Context(T target) {
		this.target = target;
	}
	
	public T getTarget() {
		return this.target;
	}
}
