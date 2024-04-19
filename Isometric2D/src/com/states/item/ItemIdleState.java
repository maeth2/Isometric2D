package com.states.item;

import java.util.List;

import org.joml.Vector3f;

import com.Main;
import com.components.AABBComponent;
import com.components.LightComponent;
import com.components.shaders.LightShaderComponent;
import com.entities.Entity;
import com.entities.items.Item;

public class ItemIdleState extends ItemState{

	public ItemIdleState(ItemContext context, ItemStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {	
		this.context.getTarget().setPivotY(0);
		this.context.getTarget().setRotationX(0);;
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Item.states.Idle);
		}
		context.getTarget().addComponent(new LightComponent(new Vector3f(50, 50, 50), 100, 1f, false));
		if(Main.getScene().getRenderer().getComponent(LightShaderComponent.class) != null) {
			Main.getScene().getRenderer().getComponent(LightShaderComponent.class).addLight(context.getTarget());
		}
		
		context.getTarget().setLayer(1);
		context.getTarget().getComponent(AABBComponent.class).setCollision(true);
	}

	@Override
	public void exit() {
		if(Main.getScene().getRenderer().getComponent(LightShaderComponent.class) != null) {
			Main.getScene().getRenderer().getComponent(LightShaderComponent.class).removeLight(context.getTarget());
		}
		context.getTarget().removeComponent(LightComponent.class);
	}

	@Override
	public void update(float dt) {
		List<Entity> e = checkCollision();
		for(Entity i : e) {
			if(i.getTrigger("Use") && i.getInventory().getSelected() == null) {
				context.getTarget().setEntity(i);
				i.getInventory().setSelected(context.getTarget());
				this.nextState = ItemStateMachine.state.Picked;
				break;
			}
		}
	}
	
	@Override
	public ItemState create(ItemContext context, ItemStateMachine.state stateKey) {
		return new ItemIdleState(context, stateKey);
	}
}
