package com.components;

import org.joml.Vector2f;

import com.utils.AssetManager;
import com.utils.Texture;

public class TextureComponent extends Component {
	private Texture texture;
	private float alpha;
	private boolean castShadow;
	private Vector2f spritePosition;
	private Vector2f spriteDimension;
	private static final Texture DEFAULT_TEXTURE = AssetManager.getTexture("assets/textures/blank.png");
	
	public TextureComponent() {
		init(DEFAULT_TEXTURE, 1f, false, new Vector2f(0, 0), DEFAULT_TEXTURE.getDimensions());
	}
	
	public TextureComponent(Texture texture) {
		init(texture, 1f, false, new Vector2f(0, 0), texture.getDimensions());
	}
	
	public TextureComponent(Texture texture, float alpha) {
		init(texture, alpha, false, new Vector2f(0, 0), texture.getDimensions());
	}

	public TextureComponent(Texture texture, boolean castShadow) {
		init(texture, 1f, castShadow, new Vector2f(0, 0), texture.getDimensions());
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow) {
		init(texture, alpha, castShadow, new Vector2f(0, 0), texture.getDimensions());
	}
	
	public TextureComponent(Texture texture, Vector2f spriteDimension) {
		init(texture, 1f, false, new Vector2f(0, 0), spriteDimension);
	}
	
	public TextureComponent(Texture texture, float alpha, Vector2f spriteDimension) {
		init(texture, alpha, false, new Vector2f(0, 0), spriteDimension);
	}

	public TextureComponent(Texture texture, boolean castShadow, Vector2f spriteDimension) {
		init(texture, 1f, castShadow, new Vector2f(0, 0), spriteDimension);
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow, Vector2f spriteDimension) {
		init(texture, alpha, castShadow, new Vector2f(0, 0), spriteDimension);
	}
	
	public TextureComponent(Texture texture, Vector2f spritePosition, Vector2f spriteDimension) {
		init(texture, 1f, false, spritePosition, spriteDimension);
	}
	
	public TextureComponent(Texture texture, float alpha, Vector2f spritePosition, Vector2f spriteDimension) {
		init(texture, alpha, false, spritePosition, spriteDimension);
	}

	public TextureComponent(Texture texture, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimension) {
		init(texture, 1f, castShadow, spritePosition, spriteDimension);
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimension) {
		init(texture, alpha, castShadow, spritePosition, spriteDimension);
	}
	
	public void init(Texture texture, float alpha, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimension) {
		this.texture = texture;
		this.alpha = alpha;
		this.castShadow = castShadow;
		this.spritePosition = spritePosition;
		this.spriteDimension = spriteDimension;
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
	
	public Vector2f getSpritePosition() {
		return this.spritePosition;
	}
	
	public void setSpritePosition(int x, int y) {
		this.spritePosition.x = x;
		this.spritePosition.y = y;
	}
	
	public Vector2f getSpriteDimension() {
		return this.spriteDimension;
	}
	
	public void setSpriteDimension(int width, int height) {
		this.spriteDimension.x = width;
		this.spriteDimension.y = height;
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
