package com.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.GameObject;
import com.Main;

import listeners.KeyListener;
import util.Sort;

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
		
		if(this.gameObject.getComponent(AABB.class) !=  null) {
			AABB aabb = this.gameObject.getComponent(AABB.class);
			aabb.setTexture(AABB.HITBOX_TEXTURE);
			
			List<Vector2f> sorted = new ArrayList<Vector2f>();
			List<GameObject> surrounding = Main.getScene().getSurroundingGridObjects(gameObject);
			for(int i = 0; i < surrounding.size(); i++) {
				GameObject o = surrounding.get(i);
				if(o.getComponent(AABB.class) != null) {
					float t = aabb.getAABBSweptCollision(vel, o.getComponent(AABB.class));
					if(t != -1) {
						sorted.add(new Vector2f(t, i));
					}
				}
			}

			sorted = Sort.sort(sorted, (a, b) -> {
				return a.x >= b.x;
			});

//			if(surrounding.size() > 0) {
//				for(Vector2f s : sorted) {
//					GameObject i = surrounding.get((int)s.y);
//					System.out.print("(" + i.getGridPosition().y + ", " + i.getGridPosition().x + ", " + s.x + ")");
//				}
//				System.out.print("\n");
//			}
			
			for(Vector2f s : sorted){
				GameObject i = surrounding.get((int) s.y);
				i.getComponent(AABB.class).setTexture(AABB.HITBOX_TEXTURE);
				if(i == this.gameObject) continue;
				Vector2f contactNormal = new Vector2f();
				float t = aabb.getAABBSweptCollision(vel, i.getComponent(AABB.class), contactNormal);
				if(t != -1) {
					vel.x += Math.abs(vel.x) * contactNormal.x * (1 - t);
					vel.y += Math.abs(vel.y) * contactNormal.y * (1 - t);
				}
			}
		}
		 
		this.gameObject.transform.position.x += vel.x;
		this.gameObject.transform.position.y += vel.y;
		
		Main.getScene().updateGrid(gameObject);
	}
	

	@Override
	public void start() {}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
