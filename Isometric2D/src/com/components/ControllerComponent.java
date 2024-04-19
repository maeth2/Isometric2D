package com.components;

import org.lwjgl.glfw.GLFW;

import com.Main;
import com.entities.Entity;
import com.listeners.KeyListener;
import com.listeners.MouseListener;
import com.states.movement.MovementStateMachine;

public class ControllerComponent extends Component {	
	private Entity entity;
	
	public ControllerComponent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void update(float dt) { 
		entity.setTrigger("Up", KeyListener.isKeyPressed(GLFW.GLFW_KEY_W));
		entity.setTrigger("Down", KeyListener.isKeyPressed(GLFW.GLFW_KEY_S));
		entity.setTrigger("Left", KeyListener.isKeyPressed(GLFW.GLFW_KEY_A));
		entity.setTrigger("Right", KeyListener.isKeyPressed(GLFW.GLFW_KEY_D));
		entity.setTrigger("Roll", KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE));
		entity.setTrigger("Use", KeyListener.isKeyPressed(GLFW.GLFW_KEY_E));
		entity.setTrigger("Drop", KeyListener.isKeyPressed(GLFW.GLFW_KEY_F));
		entity.setTrigger("L_Click", MouseListener.mouseButtonDown(0));
		entity.setTrigger("R_Click", MouseListener.mouseButtonDown(1));
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_B)) {
			entity.getStateMachine(MovementStateMachine.class).onKnockback(100f, 0f);
		}
		entity.setTargetDestination(
			MouseListener.getX() + Main.getScene().getCamera().getTransform().getPosition().x, 
			MouseListener.getY() + Main.getScene().getCamera().getTransform().getPosition().y
		);
	}
	

	@Override
	public void start() {}
}
