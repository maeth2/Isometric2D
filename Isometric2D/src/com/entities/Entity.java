package com.entities;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import com.GameObject;
import com.components.InventoryComponent;
import com.components.effects.StatusEffect;
import com.components.effects.StatusEffectManagerComponent;
import com.states.movement.MovementStateMachine;
import com.states.attack.AttackStateMachine;
import com.utils.Transform;

public abstract class Entity extends GameObject{
	protected Map<String, Boolean> trigger = new HashMap<String, Boolean>();
	protected Vector2f targetDestination = new Vector2f(0, 0);
	private float speed = 500f;
	private InventoryComponent inventory;
	private StatusEffectManagerComponent statusManager;
	
	public static enum states{
		Idle,
		Running,
		Rolling,
	}
	
	public Entity(String name, Transform transform) {
		super(name, transform, 2);
		if(transform != null) {
			addComponents();
		}
		
		this.inventory = new InventoryComponent();
		addComponent(inventory);
		this.statusManager = new StatusEffectManagerComponent();
		addComponent(statusManager);
		
		addStateMachine(new MovementStateMachine(this));
		addStateMachine(new AttackStateMachine(this, null, null, null));
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
	 * On hit effects
	 * 
	 * @param direction			Knockback direction
	 * @param knockBack			Knockback distance
	 * @param dmg				Damage dealt
	 */
	public void onHit(Vector2f direction, float knockBack, float dmg) {
		float dx = direction.x * knockBack;
		float dy = direction.y * knockBack;
		this.getStateMachine(MovementStateMachine.class).onKnockback(dx, dy);
	}
	
	/**
	 * Apply status effects
	 * 
	 * @param effect			Effect to apply
	 * @param duration			Duration of effect
	 * @param strength			Strength of effect
	 */
	public void apply(StatusEffect.effects effect, float duration, float strength) {
		statusManager.add(effect, this, duration, strength);
	}
	
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
	 * Set Target Destination
	 * 
	 * @param x			Target x coordinates
	 * @param y			Target y coordinates
	 */
	public void setTargetDestination(float x, float y) {
		this.targetDestination.x = x;
		this.targetDestination.y = y;
	}
	
	/**
	 * Get Target Destination
	 * 
	 * @return			Target Destination
	 */
	public Vector2f getTargetDestination() {
		return this.targetDestination;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public InventoryComponent getInventory() {
		return this.inventory;
	}

	/**
	 * Factory method used to create new instances of the entity
	 * 
	 * @param t			Entity Transform
	 * @return 			New instance of Entity
	 */
	public abstract Entity create(String name, Transform t);
}
