package com.utils;

import org.joml.Vector2f;

public class Transform {
	private Vector2f position;
	private Vector2f scale;
	private Vector2f rotation;
	private Vector2f pivot;
	private float zLayer = -1;
	
	public Transform() {
		init(new Vector2f(0, 0), new Vector2f(1, 1), new Vector2f(0, 0), new Vector2f(0, 0));
	}
	
	public Transform(Vector2f scale) {
		init(new Vector2f(0, 0), scale, new Vector2f(0, 0), new Vector2f(0, 0));
	}

	public Transform(Vector2f position, Vector2f scale) {
		init(position, scale, new Vector2f(0, 0), new Vector2f(0, 0));
	}

	public Transform(Vector2f position, Vector2f scale, Vector2f rotation) {
		init(position, scale, rotation, new Vector2f(0, 0));
	}
	
	public Transform(Vector2f position, Vector2f scale, Vector2f rotation, Vector2f pivot) {
		init(position, scale, rotation, pivot);
	}
	
	public void init(Vector2f position, Vector2f scale, Vector2f rotation, Vector2f pivot) {
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		this.pivot = pivot;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y) {
		if(this.position == null) {
			this.position = new Vector2f(x, y);
			return;
		}
		this.position.x = x;
		this.position.y = y;
	}
	
	public void changePosition(float x, float y) {
		this.position.x += x;
		this.position.y += y;
	}

	public Vector2f getScale() {
		return scale;
	}

	public void setScale(Vector2f scale) {
		this.scale = scale;
	}

	public Vector2f getRotation() {
		return rotation;
	}

	public void setRotation(Vector2f rotation) {
		this.rotation = rotation;
	}

	public Vector2f getPivot() {
		return pivot;
	}

	public void setPivot(Vector2f pivot) {
		this.pivot = pivot;
	}

	public float getZLayer() {
		return zLayer;
	}

	public void setZLayer(float zLayer) {
		this.zLayer = zLayer;
	}

}
