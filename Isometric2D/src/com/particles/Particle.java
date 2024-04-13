package com.particles;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.Main;
import com.components.TextureComponent;

public class Particle {
	private Vector2f position;
	private Vector2f scale;
	private TextureComponent texture;
	
	private Vector2f velocity;
	
	private float duration;
	private float startTime;
	
	public Particle(Vector2f position, Vector2f scale, Vector3f color, float duration) {
		init(position, scale, color, null, new Vector2f(0, 0), duration);
	}
	
	public Particle(Vector2f position, Vector2f scale, Vector3f color, Vector2f velocity, float duration) {
		init(position, scale, color, null, velocity, duration);
	}
	
	public Particle(Vector2f position, Vector2f scale, TextureComponent texture, float duration) {
		init(position, scale, null, texture, new Vector2f(0, 0), duration);
	}
	
	public Particle(Vector2f position, Vector2f scale, TextureComponent texture, Vector2f velocity, float duration) {
		init(position, scale, null, texture, velocity, duration);
	}
	
	public void init(Vector2f position, Vector2f scale, Vector3f color, TextureComponent texture, Vector2f velocity, float duration) {
		this.position = position;
		this.scale = scale;	
		this.velocity = velocity;
		this.duration = duration;
		this.startTime = Main.getTimeElapsed();
		
		if(texture != null) {
			this.texture = texture;
		}else {
			this.texture = new TextureComponent(color);
		}
	}
	
	public void update(float dt) {
		position.x += velocity.x * dt;
		position.y += velocity.y * dt;
	}
	
	public Vector2f getPosition() {
		return this.position;
	}
	
	public Vector2f getScale() {
		return this.scale;
	}
	
	public TextureComponent getTexture() {
		return this.texture; 
	}
	
	public boolean checkDuration() {
		return this.startTime + duration < Main.getTimeElapsed();
	}
}
