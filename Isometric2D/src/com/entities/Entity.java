package com.entities;

import java.util.HashMap;
import java.util.Map;

import com.GameObject;
import com.states.movement.MovementStateMachine;
import com.states.attack.AttackStateMachine;
import com.utils.Transform;

public abstract class Entity extends GameObject{
	protected Map<String, Boolean> trigger = new HashMap<String, Boolean>();
	private float speed = 500f;
	
	public Entity(String name, Transform transform) {
		super(name, transform);
		if(transform != null) {
			addComponents();
		}
		addStateMachine(new MovementStateMachine(this));
		addStateMachine(new AttackStateMachine(this));
		addTrigger("Use");
		addTrigger("Drop");
	}
	
	public Entity(String name) {
		super(name);
		addStateMachine(new MovementStateMachine(this));
		addTrigger("Use");
		addTrigger("Drop");
	}

	/**
	 * Adds neccessary components to entity
	 */
	public abstract void addComponents();
	
	/**
	 * Add trigger to trigger list
	 * 
	 * @param i			Action to add
	 */
	public void addTrigger(String i) {
		if(!trigger.containsKey(i)) {
			trigger.put(i, false);
		}
	}
	
	/**
	 * Set trigger
	 * 
	 * @param i			Action to set
	 * @param j			Action state
	 */
	public void setTrigger(String i, boolean j) {
		trigger.put(i, j);
	}
	
	/**
	 * Get trigger
	 * 
	 * @param i			Trigger to check
	 * 
	 * @return
	 */
	public boolean getTrigger(String i) {
		return trigger.get(i);
	}
	
	/**
	 * Factory method used to create new instances of the entity
	 * 
	 * @param t			Entity Transform
	 * @return 			New instance of Entity
	 */
	public abstract Entity create(String name, Transform t);
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
