package com.components;

import org.lwjgl.glfw.GLFW;

import com.entities.Entity;
import com.listeners.KeyListener;

public class ControllerComponent extends Component {	
	private Entity entity;
	
	public ControllerComponent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void update(float dt) { 
		entity.setAction("Up", KeyListener.isKeyPressed(GLFW.GLFW_KEY_W));
		entity.setAction("Down", KeyListener.isKeyPressed(GLFW.GLFW_KEY_S));
		entity.setAction("Left", KeyListener.isKeyPressed(GLFW.GLFW_KEY_A));
		entity.setAction("Right", KeyListener.isKeyPressed(GLFW.GLFW_KEY_D));
		entity.setAction("Roll", KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE));
	}
	

	@Override
	public void start() {}
}
