package com.entities;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.GameObject;
import com.Main;
import com.components.InventoryComponent;
import com.components.effects.StatusEffect;
import com.components.effects.StatusEffectManagerComponent;
import com.scenes.Scene;
import com.states.movement.MovementStateMachine;
import com.states.ai.EnemyStateMachine;
import com.states.attack.AttackStateMachine;
import com.utils.Transform;

public abstract class Entity extends GameObject{
	protected Map<String, Boolean> trigger = new HashMap<String, Boolean>();
	protected Vector2f targetDestination = new Vector2f(0, 0);
	private InventoryComponent inventory;
	private StatusEffectManagerComponent statusManager;
	
	private float baseSpeed = 500f;
	private float speedModifier = 1f;
	
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
		
		addStateMachine(new MovementStateMachine(this));
		addStateMachine(new AttackStateMachine(this, null, null, null));
		addTrigger("Use");
		addTrigger("Drop");
		addTrigger("Damaged");
		
		this.statusManager = new StatusEffectManagerComponent();
		addComponent(statusManager);
	}
	
	public Entity(String name) {
		super(name);
		addStateMachine(new MovementStateMachine(this));
		addTrigger("Use");
		addTrigger("Drop");
		addTrigger("Damaged");
	}
	
	/**
	 * Update funtion
	 * 
	 * @param dt		Delta time
	 */
	public void update(float dt) {
		updateComponents(dt);
		updateStateMachines(dt);
		if(getTrigger("Damaged")) {
			setTrigger("Damaged", false);
		}
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
	public void onHit(Entity origin, Vector2f direction, float knockBack, float dmg) {
		float dx = direction.x * knockBack;
		float dy = direction.y * knockBack;
		MovementStateMachine move = this.getStateMachine(MovementStateMachine.class);
		if(move != null) {
			move.onKnockback(dx, dy);
		}
		EnemyStateMachine ai = this.getStateMachine(EnemyStateMachine.class);
		if(ai != null) {
			ai.setTargetEntity(origin);
		}
		onDamage(dmg);
	}
	
	/**
	 * On Damage
	 * 
	 * @param dmg				Damage dealt
	 */
	public void onDamage(float dmg) {
		setTrigger("Damaged", true);
		Main.getScene().getParticles().add(
			Float.toString(dmg), 
			new Vector2f(
				transform.getPosition().x + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f, 
				transform.getPosition().y + Main.random.nextFloat() * Scene.UNIT_SIZE / 4f
			),
			12f, 
			new Vector2f(0, 100f),
			0.3f,
			new Vector3f(1, 1, 1)
		);
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

	public void setBaseSpeed(float speed) {
		this.baseSpeed = speed;
	}
	
	public void setSpeedModifier(float i) {
		this.speedModifier = i;
	}
	
	public float getSpeedModifier() {
		return this.speedModifier;
	}
	
	public float getSpeed() {
		return this.baseSpeed * this.speedModifier;
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
