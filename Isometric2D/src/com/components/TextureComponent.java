package com.components;

import util.AssetManager;
import util.Texture;

public class TextureComponent extends Component {
	private Texture texture;
	private float alpha;
	private boolean castShadow;
	
	public TextureComponent() {
		init(AssetManager.getTexture("assets/textures/blank.png"), 1f, false);
	}
	
	public TextureComponent(Texture texture) {
		init(texture, 1f, false);
	}
	
	public TextureComponent(Texture texture, float alpha) {
		init(texture, alpha, false);
	}

	public TextureComponent(Texture texture, boolean castShadow) {
		init(texture, 1f, castShadow);
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow) {
		init(texture, alpha, castShadow);
	}
	
	public void init(Texture texture, float alpha, boolean castShadow) {
		this.texture = texture;
		this.alpha = alpha;
		this.castShadow = castShadow;
	}
	
	@Override
	public void update(float dt) {
	}

	@Override
	public void start() {		
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	
	public boolean canCastShadow() {
		return castShadow;
	}

	public void setCastShadow(boolean castShadow) {
		this.castShadow = castShadow;
	}

	
}
