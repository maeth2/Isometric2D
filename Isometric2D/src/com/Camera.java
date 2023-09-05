 package com;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.components.ControllerComponent;

import util.Maths;

public class Camera extends GameObject{
	private Matrix4f projectionMatrix;
	private GameObject target;
	private float zoom = 1;
	
	public Camera(Vector2f position) {
		super("Camera");
		target = this;
		this.transform.position = position;
		this.projectionMatrix = new Matrix4f();
		this.addComponent(new ControllerComponent());
		createOrthographicProjection(Window.WIDTH, Window.HEIGHT);
	}
	
	@Override
	public void update(float dt) {
		if(target.getComponent(ControllerComponent.class) != null) {
			target.getComponent(ControllerComponent.class).updateController(dt);
		}
		this.setPosition(target.transform.position);
	}

	public void createOrthographicProjection(float width, float height) {
		float w = width / 2 / zoom;
		float h = height / 2 / zoom;
	    projectionMatrix = new Matrix4f().ortho(-w, w, -h, h, 0, 1);
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
		this.setPosition(target.transform.position);
	}
	
	public void changeZoom(float i) {
		this.zoom += i;
		if(this.zoom <= 0) {
			this.zoom = 0.001f;
		}
		this.createOrthographicProjection(Window.WIDTH, Window.HEIGHT);
	}
	
	public void setPosition(Vector2f position) {
		this.transform.position.x = position.x;
		this.transform.position.y = position.y;
	}
}
