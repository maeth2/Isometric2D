package com.states;

public abstract class State<T extends Enum<T>> {
	protected T stateKey;
	protected T nextState;
	
	public State(T stateKey) {
		this.stateKey = stateKey;
	}
	
	public abstract void enter();
	public abstract void exit();
	public abstract void update(float dt);
	public abstract T next();
	
	public T getStateKey() {
		return this.stateKey;
	}
}
