package com.states.item;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.GameObject;
import com.Main;
import com.components.AABBComponent;
import com.entities.Entity;
import com.states.State;
import com.utils.Animation;

public abstract class ItemState extends State<ItemStateMachine.state> {
	protected ItemContext context;
	protected Animation frames;
	protected List<Entity> surroundingEntity = new ArrayList<Entity>();


	public ItemState(ItemContext context, ItemStateMachine.state stateKey) {
		super(stateKey);
		this.context = context;
	}
	
	public List<Entity> checkCollision() {
		surroundingEntity.clear();
		GameObject entity = context.getTarget();
		AABBComponent aabb = entity.getComponent(AABBComponent.class);
		if(aabb != null) {
			List<GameObject> surrounding = Main.getScene().getSurroundingGrid(entity, 2, 2);
			for(GameObject i : surrounding) {
				if(i == entity) continue;
				if(!(i instanceof Entity)) continue;
				Entity e = (Entity) i;
				if(e == context.getTarget().getEntity()) continue;
				if(aabb.getCollision(e.getComponent(AABBComponent.class))) {
					surroundingEntity.add(e);
				}
			}
		}
		return surroundingEntity;
	}
	
	public void pointToDestination() {
		float sx = this.context.getTarget().transform.position.x;
		float sy = this.context.getTarget().transform.position.y;
		float mx = context.getTarget().getEntity().getTargetDestination().x;
		float my = context.getTarget().getEntity().getTargetDestination().y - context.getTarget().transform.scale.y;
		float angle = (float) Math.toDegrees(Math.atan2((my - sy), (mx - sx))) - 90f;
		this.context.getTarget().transform.rotation.x = angle;

		context.getTarget().transform.scale.x = Math.abs(context.getTarget().transform.scale.x) * (mx > sx ? -1 : 1);
		
		this.context.getTarget().setDirty(true);
	}
	
	public void stickToEntity() {
		Vector2f entityPos = context.getTarget().getEntity().transform.position;
		Vector2f entityScale = context.getTarget().getEntity().transform.scale;
		
		this.context.getTarget().transform.position.x = entityPos.x - entityScale.x * 0.3f;
		this.context.getTarget().transform.position.y = entityPos.y - entityScale.y * 0.2f;
		this.context.getTarget().transform.zLayer = entityPos.y - entityScale.y * 0.001f;
		
		Main.getScene().updateGrid(context.getTarget());
		this.context.getTarget().setDirty(true);
	}
	
	public ItemState create(ItemContext context, ItemStateMachine.state stateKey) {
		return null;
	}
}
