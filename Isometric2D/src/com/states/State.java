package com.states;

public abstract class State<T extends Enum<T>> {
	protected T stateKey;
	protected T nextState;
	
	protected float entryTime;
	protected float cooldown = 0f;
	
	public State(T stateKey) {
		this.stateKey = stateKey;
	}
		
	public abstract void enter();
	public abstract void exit();
	public abstract void update(float dt);
	
	public T next() {
		return this.nextState;
	}
	
	public T getStateKey() {
		return this.stateKey;
	}
	
	public void setNextState(T i) {
		this.nextState = i;
	}
	
	public boolean checkCooldown(float dt) {
		return (dt - entryTime) > cooldown;
	}
}
