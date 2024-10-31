package com.states;

import java.util.HashMap;
import java.util.Map;

import com.GameObject;
import com.Main;

public abstract class StateMachine<T extends Enum<T>, S extends GameObject, U extends Context<?>> {
	protected Map<T, State<T>> states = new HashMap<T, State<T>>();
	protected State<T> currentState;
	protected Context<S> context;
	
	/**
	 * Initialize State Machine
	 * 
	 * @param currentState			Initial State of the Machine
	 * @param context				State Context
	 */
	public StateMachine(T currentState, Context<S> context) {
		this.context = context;
		initialiseStates();
		this.currentState = states.get(currentState);
	}
	
	/**
	 * Start the State Machine
	 */
	public void start() {
		currentState.setNextState(currentState.stateKey); 
		currentState.enter();
	}
	
	/**
	 * Update the State Machine
	 * 
	 * @param dt		Delta Time
	 */
	public void update(float dt) {
	/*
	 * States Work by checking what the next State is. If the next state is the current one, keep updating it. 
	 * Otherwise, transition to the next state.
	 */
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

	/**
	 * Transition States
	 * 
	 * @param nextState			Next State
	 */
	public void transition(T nextState) {
		currentState.exit();
		if(states.get(nextState) != null) {
			currentState = states.get(nextState);
			currentState.setNextState(nextState);
		}
		currentState.enter();
	}
	
	/**
	 * Manual State Transition
	 * 
	 * @param stateKey			Next State
	 */
	public void setState(T stateKey) {
		transition(stateKey);
	}
	
	/**
	 * Get Context of the State Machine
	 * 
	 * @return			Current Context of State Machine
	 */
	@SuppressWarnings("unchecked")
	public U getContext() {
		return (U) this.context;
	}
	
	/**
	 * Add State to State Machine
	 * 
	 * @param state				State Enum to Add
	 * @param s					State Object to Add
	 */
	public void addState(T state, State<T> s) {
		states.put(state, s);
	}
	
	public abstract void initialiseStates();
}
