package com.components;

import org.joml.Vector3f;

public class LightComponent extends Component {
	private Vector3f color;
	private float radius;
	private boolean castShadow;
	private float intensity;
	
	public LightComponent(float radius) {
		init(new Vector3f(1, 1, 1), radius, true, 1);
	}
	
	public LightComponent(float radius, boolean castShadow) {
		init(new Vector3f(1, 1, 1), radius, castShadow, 1);
	}
	
	public LightComponent(Vector3f color, float radius) {
		init(color, radius, true, 1);
	}
	
	public LightComponent(Vector3f color, float radius, float intensity) {
		init(color, radius, true, intensity);
	}
	
	public LightComponent(Vector3f color, float radius, boolean castShadow) {
		init(color, radius, castShadow, 1);
	}
	
	public LightComponent(Vector3f color, float radius, float intensity, boolean castShadow) {
		init(color, radius, castShadow, intensity);
	}

	public void init(Vector3f color, float radius, boolean castShadow, float intensity) {
		this.color = color.div(255);
		this.radius = radius;
		this.castShadow = castShadow;
		this.intensity = intensity;
	}
	
	public boolean canCastShadow() {
		return castShadow;
	}

	public void setCastShadow(boolean castShadow) {
		this.castShadow = castShadow;
	}
	
	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	@Override
	public void start() {		
	}

	@Override
	public void update(float dt) {

	}

}
