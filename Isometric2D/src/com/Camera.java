 package com;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.components.ControllerComponent;

import util.Maths;

public class Camera extends GameObject{
	private Matrix4f projectionMatrix;
	private GameObject target;
	private float zoom = 100;
	
	public Camera(Vector2f position) {
		super("Camera");
		target = this;
		this.transform.position = position;
		this.projectionMatrix = new Matrix4f();
		this.addComponent(new ControllerComponent());
		createOrthographicProjection();
	}
	
	@Override
	public void update(float dt) {
		if(target.getComponent(ControllerComponent.class) != null) {
			target.getComponent(ControllerComponent.class).updateController(dt);
		}
		this.setPosition(target.transform.position);
	}

	public void createOrthographicProjection() {
		float w = Window.WIDTH / 2 / zoom;
		float h = Window.HEIGHT / 2 / zoom;
	    projectionMatrix = new Matrix4f().ortho2D(-w, w, -h, h);
	}
	
	public Matrix4f getViewMatrix() {
		return Maths.createViewMatrix(this.transform);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setTarget(GameObject target) {
		this.target = target;
	}
	
	public void changeZoom(float i) {
		this.zoom += i;
		if(this.zoom <= 0) {
			this.zoom = 1;
		}
		this.createOrthographicProjection();
	}
	
	public void setPosition(Vector2f position) {
		this.transform.position.x = position.x;
		this.transform.position.y = position.y;
	}
}
