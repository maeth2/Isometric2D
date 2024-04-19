 package com;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.components.Component;
import com.components.ControllerComponent;
import com.entities.Entity;
import com.utils.Maths;
import com.utils.Transform;

public class Camera extends Entity{
	private Matrix4f projectionMatrix;
	private GameObject target;

	private boolean freeCam = false;
	private GameObject prevTarget;
	
	public Camera(Vector2f position) {
		super("Camera");
		target = this;
		this.transform.setPosition(position);
		projectionMatrix = Maths.createOrthographicProjection(Window.WIDTH, Window.HEIGHT);
	}
	
	@Override
	public void update(float dt) {
		this.setPosition(target.transform.getPosition());
		for(Component c : components) {
			c.update(dt);
		}
	}
	
	public Matrix4f getViewMatrix() {
		return Maths.createViewMatrix(this.transform);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public GameObject getTarget() {
		return this.target;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
		this.setPosition(target.transform.getPosition());
	}
	
	public void setPosition(Vector2f position) {
		this.transform.getPosition().x = position.x;
		this.transform.getPosition().y = position.y;
	}
	
	public void toggleFreeCam() {
		if(freeCam) {
			freeCam = false;
			this.setTarget(prevTarget);
			if(this.getComponent(ControllerComponent.class) != null) {
				this.removeComponent(ControllerComponent.class);
			}
		}else {
			freeCam = true;
			prevTarget = target;
			this.setTarget(this);
			if(this.getComponent(ControllerComponent.class) == null) {
				this.addComponent(new ControllerComponent(this));
				this.getComponent(ControllerComponent.class);
			}
		}
	}

	@Override
	public void addComponents() {}

	@Override
	public Entity create(String name, Transform t) {
		return null;
	}
}
