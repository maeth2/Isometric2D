package com.entities.items.weapons.syringe;

import org.joml.Vector2f;

import com.Main;
import com.entities.items.Item;
import com.scenes.Scene;
import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;
import com.utils.Maths;

public class SyringeAttackState extends ItemState{	
	private SyringeProjectile projectile = new SyringeProjectile(
			"Syringe", 
			null, 
			null,
			0f,
			null
	);
	
	public SyringeAttackState(ItemContext context, ItemStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Item.states.Use);
		}
		
		Vector2f direction = Maths.angleToDirectionVector(context.getTarget().getTransform().getRotation().x + 90f);
		Vector2f position = new Vector2f(
				context.getTarget().getTransform().getPosition().x + direction.x * Math.abs(context.getTarget().getTransform().getScale().x), 
				context.getTarget().getTransform().getPosition().y + direction.y * Math.abs(context.getTarget().getTransform().getScale().y)
		);
		Vector2f scale = new Vector2f(Scene.UNIT_SIZE * 0.25f, Scene.UNIT_SIZE * 0.25f);

		Main.getScene().addGameObject(projectile.create(
			"Syringe",
			position,
			scale,
			direction,
			1000f,
			context.getTarget().getEntity()
		));
		
		cooldown = context.getAnimation().getCurrentAnimation().getAnimationDuration();
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		pointToDestination();
		stickToEntity();
		if(context.getAnimation().getCurrentFrame() == -1) {
			nextState = ItemStateMachine.state.Picked;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.state stateKey) {
		return new SyringeAttackState(context, stateKey);
	}
}
