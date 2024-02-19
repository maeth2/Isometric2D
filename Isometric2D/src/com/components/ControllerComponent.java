package com.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.GameObject;
import com.Main;

import listeners.KeyListener;
import util.Helper;

public class ControllerComponent extends Component {
	private float speed = 500f;
	private Vector2f vel = new Vector2f(0, 0);
	
	@Override
	public void update(float dt) {
		vel.x = 0;
		vel.y = 0;
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)){
			vel.y = speed * dt;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
			vel.y = -speed * dt;
		}
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)){
			vel.x = -speed * dt;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
			vel.x = speed * dt;
		}
		
		if(this.gameObject.getComponent(AABBComponent.class) !=  null) {
			AABBComponent aabb = this.gameObject.getComponent(AABBComponent.class);
			aabb.setTexture(AABBComponent.HITBOX_TEXTURE);
			
			List<Vector2f> sorted = new ArrayList<Vector2f>();
			List<GameObject> surrounding = Main.getScene().getSurroundingLevel(gameObject, 2, 2);
			for(int i = 0; i < surrounding.size(); i++) {
				GameObject o = surrounding.get(i);
				if(o.getComponent(AABBComponent.class) != null && o != this.gameObject) {
					float t = aabb.getAABBSweptCollision(vel, o.getComponent(AABBComponent.class));
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
				i.getComponent(AABBComponent.class).setTexture(AABBComponent.HITBOX_TEXTURE);
				if(i == this.gameObject) continue;
				Vector2f contactNormal = new Vector2f();
				float t = aabb.getAABBSweptCollision(vel, i.getComponent(AABBComponent.class), contactNormal);
				if(t != -1) {
					vel.x += Math.abs(vel.x) * contactNormal.x * (1 - t);
					vel.y += Math.abs(vel.y) * contactNormal.y * (1 - t);
				}
			}
		}
		 
		this.gameObject.transform.position.x += vel.x;
		this.gameObject.transform.position.y += vel.y;
		
		gameObject.setDirty(true);
		Main.getScene().updateGrid(gameObject);
	}
	

	@Override
	public void start() {}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
