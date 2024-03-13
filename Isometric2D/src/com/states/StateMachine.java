package com.states;

import java.util.HashMap;
import java.util.Map;

import com.GameObject;
import com.Main;

public abstract class StateMachine<T extends Enum<T>, S extends GameObject, U extends Context<?>> {
	protected Map<T, State<T>> states = new HashMap<T, State<T>>();
	protected State<T> currentState;
	private Context<S> context;
	
	public StateMachine(T currentState, Context<S> context) {
		this.context = context;
		initialiseStates();
		this.currentState = states.get(currentState);
	}
	
	public void start() {
		currentState.setNextState(currentState.stateKey);
		currentState.enter();
	}
	
	public void update(float dt) {
		T nextState = currentState.next();
		if(nextState == currentState.getStateKey()) {
			currentState.update(dt);
		}else if(states.get(nextState).checkCooldown(Main.getTimeElapsed())){
			transition(nextState);
		}else {
			currentState.nextState = currentState.stateKey;
			currentState.update(dt);
		}
	}

	public void transition(T nextState) {
		currentState.exit();
		if(states.get(nextState) != null) {
			currentState = states.get(nextState);
			currentState.setNextState(nextState);
		}
		currentState.enter();
	}
	
	public void setState(T stateKey) {
		transition(stateKey);
	}
	
	@SuppressWarnings("unchecked")
	public U getContext() {
		return (U) this.context;
	}
	
	public void addState(T state, State<T> s) {
		states.put(state, s);
	}
	
	public abstract void initialiseStates();
}
