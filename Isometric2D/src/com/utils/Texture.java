package com.utils;

import org.joml.Vector2f;

public class Texture {
	public static final int TYPE_OUTPUT = 0;
	public static final int TYPE_COLOR = 1;
	public static final int TYPE_ALPHA = 2;

	private int id;
	private Vector2f dimensions;
	private int type;

	public Texture(int id, int width, int height) {
		init(0, id, width, height);
	}
	
	public Texture(int type, int id, int width, int height) {
		init(type, id, width, height);
	}
	
	public Texture() {
		init(0, 0, 0, 0);
	}
	
	public void init(int type, int id, int width, int height) {
		this.type = type;
		this.id = id;
		this.dimensions = new Vector2f(width, height);
	}
	
	public void copy(Texture t) {
		this.id = t.getID();
		this.dimensions = new Vector2f(t.getWidth(), t.getHeight());
		this.type = t.getType();
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}
	
	public float getWidth() {
		return this.dimensions.x;
	}

	public void setWidth(int width) {
		this.dimensions.x = width;
	}

	public float getHeight() {
		return this.dimensions.y;
	}

	public void setHeight(int height) {
		this.dimensions.y = height;
	}

	public Vector2f getDimensions() {
		return this.dimensions;
	}
	
	public void setDimensions(float width, float height) {
		this.dimensions.x = width;
		this.dimensions.y = height;
	}
	
	public int getType() {
		return this.type;
	}
	
	public float getAspectRatio() {
		return this.dimensions.x / this.dimensions.y;
	}
}
