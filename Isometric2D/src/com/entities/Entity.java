package com.entities;

import java.util.HashMap;
import java.util.Map;

import com.GameObject;
import com.states.movement.MovementStateMachine;
import com.utils.Transform;

public abstract class Entity extends GameObject{
	protected Map<String, Boolean> actions = new HashMap<String, Boolean>();
	private float speed = 500f;
	
	public Entity(String name, Transform transform) {
		super(name, transform);
		addStateMachine(new MovementStateMachine(this));
	}
	
	public Entity(String name) {
		super(name);
		addStateMachine(new MovementStateMachine(this));
	}

	/**
	 * Adds neccessary components to entity
	 */
	public abstract void addComponents();
	
	/**
	 * Add Action to action list
	 * 
	 * @param i			Action to add
	 */
	public void addActions(String i) {
		if(!actions.containsKey(i)) {
			actions.put(i, false);
		}
	}
	
	/**
	 * Set Action
	 * 
	 * @param i			Action to set
	 * @param j			Action state
	 */
	public void setAction(String i, boolean j) {
		actions.put(i, j);
	}
	
	/**
	 * Factory method used to create new instances of the entity
	 * 
	 * @param t			Entity Transform
	 * @return 			New instance of Entity
	 */
	public abstract Entity create(Transform t);
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public boolean getActions(String direction) {
		return actions.get(direction);
	}
}
