package com.states.item;

import java.util.List;

import org.joml.Vector3f;

import com.GameObject;
import com.Main;
import com.components.AABBComponent;
import com.components.AnimationComponent;
import com.components.LightComponent;
import com.components.shaders.LightShaderComponent;
import com.entities.Entity;

public class ItemIdleState extends ItemState{

	public ItemIdleState(ItemContext context, ItemStateMachine.itemStates stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {	
		this.nextState = stateKey;
		this.context.getItem().transform.pivot.y = 0f;
		this.context.getItem().transform.rotation.x = 0f;
		this.context.getItem().setDirty(true);
		AnimationComponent a = context.getItem().getComponent(AnimationComponent.class);
		if(a != null) {
			a.setCurrentAnimation("Idle");
		}
		context.getItem().addComponent(new LightComponent(new Vector3f(50, 50, 50), 100, 1f, false));
		if(Main.getScene().getRenderer().getComponent(LightShaderComponent.class) != null) {
			Main.getScene().getRenderer().getComponent(LightShaderComponent.class).addLight(context.getItem());
		}
	}

	@Override
	public void exit() {
		if(Main.getScene().getRenderer().getComponent(LightShaderComponent.class) != null) {
			Main.getScene().getRenderer().getComponent(LightShaderComponent.class).removeLight(context.getItem());
		}
		context.getItem().removeComponent(LightComponent.class);
	}

	@Override
	public void update(float dt) {
		Entity e = checkCollision();
		if(e != null) {
			context.getItem().setEntity(e);
			this.nextState = ItemStateMachine.itemStates.Picked;
		}
	}
	
	public Entity checkCollision() {
		GameObject entity = context.getItem();
		AABBComponent aabb = entity.getComponent(AABBComponent.class);
		if(aabb != null) {
			List<GameObject> surrounding = Main.getScene().getSurroundingGrid(entity, 2, 2);
			for(GameObject i : surrounding) {
				if(i == entity) continue;
				if(!(i instanceof Entity)) continue;
				Entity e = (Entity) i;
				if(aabb.getCollision(e.getComponent(AABBComponent.class))) {
					if(e.getTrigger("Use")) {
						return e;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public ItemState create(ItemContext context, ItemStateMachine.itemStates stateKey) {
		return new ItemIdleState(context, stateKey);
	}
}
