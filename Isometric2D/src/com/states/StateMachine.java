package com.states;

import java.util.HashMap;
import java.util.Map;

public abstract class StateMachine<T extends Enum<T>> {
	protected Map<T, State<T>> states = new HashMap<T, State<T>>();
	protected State<T> currentState;
	
	public void start() {
		currentState.enter();
	}
	
	public void update(float dt) {
		T nextState = currentState.next();
		if(nextState == currentState.getStateKey()) {
			currentState.update(dt);
		}else {
			transition(nextState);
		}
	}
	
	public void transition(T nextState) {
		currentState.exit();
		if(states.get(nextState) != null) {
			currentState = states.get(nextState);
		}
		currentState.enter();
	}
	
	public abstract void initialiseStates();
}
