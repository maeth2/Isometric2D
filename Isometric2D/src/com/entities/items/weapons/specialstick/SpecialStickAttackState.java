package com.entities.items.weapons.specialstick;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.components.effects.StatusEffect;
import com.components.effects.StatusEffect.effects;
import com.entities.Entity;
import com.entities.items.Item;
import com.states.item.ItemContext;
import com.states.item.ItemState;
import com.states.item.ItemStateMachine;
import com.utils.Maths;

public class SpecialStickAttackState extends ItemState{	
	
	private List<Entity> contacted = new ArrayList<Entity>();
	
	public SpecialStickAttackState(ItemContext context, ItemStateMachine.state stateKey) {
		super(context, stateKey);
	}

	@Override
	public void enter() {
		contacted.clear();
		if(context.getAnimation() != null) {
			context.getAnimation().setCurrentAnimation(Item.states.Use);
		}
		
		context.getTarget().setScaleX(context.getTarget().getTransform().getScale().x * 2);
		context.getTarget().setScaleY(context.getTarget().getTransform().getScale().y * 2);
		context.getTarget().setPivotY(0f);
		this.cooldown = 1f;
	}

	@Override
	public void exit() {
	}

	@Override
	public void update(float dt) {
		stickToEntity();
		Vector2f direction = Maths.angleToDirectionVector(context.getTarget().getTransform().getRotation().x + 90f);
		float dx = direction.x * Math.abs(context.getTarget().getTransform().getScale().x) * 0.2f;
		float dy = direction.y * context.getTarget().getTransform().getScale().y * 0.2f;
		context.getTarget().changePosition(dx, dy);
		
		if(context.getAnimation().getCurrentAnimation().inActionFrame(context.getAnimation().getCurrentFrame())) {
			List<Entity> e = this.checkCollision();
			if(!e.isEmpty()) {
				for(Entity i : e) {
					if(!contacted.contains(i)) {
						contacted.add(i);
//						Vector2f d = Maths.pointToPointDirectionVector(i.getTransform().position, context.getTarget().getEntity().getTransform().position);
						i.onHit(context.getTarget().getEntity(), direction, 50f, 100f);
						i.apply(StatusEffect.effects.Burn, 3f, 2f);
						i.apply(effects.Slow, 0.5f, 2f);
					}
				}
			}
		}
		
		
		if(context.getAnimation().getCurrentFrame() == -1) {
			context.getTarget().setScaleX(context.getTarget().getTransform().getScale().x / 2);
			context.getTarget().setScaleY(context.getTarget().getTransform().getScale().y / 2);
			this.nextState = ItemStateMachine.state.Picked;
		}
	}

	@Override
	public ItemState create(ItemContext context, ItemStateMachine.state stateKey) {
		return new SpecialStickAttackState(context, stateKey);
	}
}
