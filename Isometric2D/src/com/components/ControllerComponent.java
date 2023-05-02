package com.components;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import listeners.KeyListener;

public class ControllerComponent extends Component {
	private float speed = 5f;
	public Vector2f vel;
	
	public void updateController(float dt) {
		vel.x = 0;
		vel.y = 0;
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)){
			vel.y = speed;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)) {
			vel.y = -speed;
		}
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)){
			vel.x = -speed;
		}else if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)) {
			vel.x = speed;
		}
		gameObject.transform.position.x += vel.x * dt;
		gameObject.transform.position.y += vel.y * dt;
	}
	
	@Override
	public void update(float dt) {}

	@Override
	public void start() {
		vel = new Vector2f();
	}

}
