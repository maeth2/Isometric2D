package com.states.movement;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.GameObject;
import com.Main;
import com.components.AABBComponent;
import com.entities.Entity;
import com.states.State;
import com.utils.Animation;
import com.utils.Helper;

public abstract class MovementState extends State<MovementStateMachine.state>{
	protected MovementContext context;
	protected Vector2f velocity = new Vector2f(0, 0);
	protected Animation frames;

	public MovementState(MovementContext context, MovementStateMachine.state stateKey) {
		super(stateKey);
		this.context = context;
	}
	
	public int pointToTarget() {
		int direction = 0;
		if(context.getTarget().getTargetDestination().x < context.getTarget().transform.position.x) {
			context.getTarget().transform.scale.x = Math.abs(context.getTarget().transform.scale.x);
			direction = 1;
		}else {
			context.getTarget().transform.scale.x = -Math.abs(context.getTarget().transform.scale.x);
			direction = -1;
		}
		context.getTarget().setDirty(true);
		return direction;
	}
	
	public void checkCollision() {
		Entity entity = context.getTarget();
		AABBComponent aabb = entity.getComponent(AABBComponent.class);
		if(aabb !=  null) {
			List<Vector2f> sorted = new ArrayList<Vector2f>();
			List<GameObject> surrounding = Main.getScene().getSurroundingLevel(entity, 2, 2);
			for(int i = 0; i < surrounding.size(); i++) {
				GameObject o = surrounding.get(i);
				AABBComponent other = o.getComponent(AABBComponent.class);
				if(other != null && o != entity) {
					float t = aabb.getAABBSweptCollision(this.velocity, o.getComponent(AABBComponent.class));
					if(t != -1) {
						sorted.add(new Vector2f(t, i));
					}
				}
			}

			sorted = Helper.sort(sorted, (a, b) -> {
				return a.x >= b.x;
			});

			for(Vector2f s : sorted){
				GameObject i = surrounding.get((int) s.y);
				if(i == entity) continue;
				Vector2f contactNormal = new Vector2f();
				float t = aabb.getAABBSweptCollision(this.velocity, i.getComponent(AABBComponent.class), contactNormal);
				if(t != -1) {
					this.velocity.x += Math.abs(this.velocity.x) * contactNormal.x * (1 - t);
					this.velocity.y += Math.abs(this.velocity.y) * contactNormal.y * (1 - t);
				}
			}
		}
	}
}
