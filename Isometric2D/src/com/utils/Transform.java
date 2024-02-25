package com.utils;

import org.joml.Vector2f;

public class Transform {
	public Vector2f position;
	public Vector2f scale;
	public Vector2f rotation;
	public Vector2f pivot;
	
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
}
