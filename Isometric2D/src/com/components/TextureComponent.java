package com.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.utils.AssetManager;
import com.utils.Texture;

public class TextureComponent extends Component {
	private Texture texture;
	private float alpha;
	private boolean castShadow;
	private Vector2f spritePosition;
	private Vector2f spriteDimensions;
	private Vector3f color = new Vector3f(-1, -1, -1);
	
	private static final Texture DEFAULT_TEXTURE = AssetManager.getTexture("assets/textures/blank.png");
	
	public TextureComponent() {
		init(DEFAULT_TEXTURE, 1f, false, new Vector2f(0, 0), DEFAULT_TEXTURE.getDimensions());
	}
	
	public TextureComponent(Vector3f color) {
		init(DEFAULT_TEXTURE, 1f, false, new Vector2f(0, 0), DEFAULT_TEXTURE.getDimensions());
		this.setColor(color.x, color.y, color.z);
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
	
	public TextureComponent(Texture texture, Vector2f spriteDimensions) {
		init(texture, 1f, false, new Vector2f(0, 0), spriteDimensions);
	}
	
	public TextureComponent(Texture texture, float alpha, Vector2f spriteDimensions) {
		init(texture, alpha, false, new Vector2f(0, 0), spriteDimensions);
	}

	public TextureComponent(Texture texture, boolean castShadow, Vector2f spriteDimensions) {
		init(texture, 1f, castShadow, new Vector2f(0, 0), spriteDimensions);
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow, Vector2f spriteDimensions) {
		init(texture, alpha, castShadow, new Vector2f(0, 0), spriteDimensions);
	}
	
	public TextureComponent(Texture texture, Vector2f spritePosition, Vector2f spriteDimensions) {
		init(texture, 1f, false, spritePosition, spriteDimensions);
	}

	public TextureComponent(Texture texture, float alpha, Vector2f spritePosition, Vector2f spriteDimensions) {
		init(texture, alpha, false, spritePosition, spriteDimensions);
	}

	public TextureComponent(Texture texture, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimensions) {
		init(texture, 1f, castShadow, spritePosition, spriteDimensions);
	}
	
	public TextureComponent(Texture texture, float alpha, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimensions) {
		init(texture, alpha, castShadow, spritePosition, spriteDimensions);
	}
	
	public void init(Texture texture, float alpha, boolean castShadow, Vector2f spritePosition, Vector2f spriteDimensions) {
		this.texture = texture;
		this.alpha = alpha;
		this.castShadow = castShadow;
		this.spritePosition = spritePosition;
		this.spriteDimensions = spriteDimensions;
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
		this.gameObject.setDirty(true);
	}
	
	public Vector2f getSpriteDimensions() {
		return this.spriteDimensions;
	}
	
	public void setSpriteDimensions(int width, int height) {
		this.spriteDimensions.x = width;
		this.spriteDimensions.y = height;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		this.gameObject.setDirty(true);
	}
	
	public boolean canCastShadow() {
		return castShadow;
	}

	public void setCastShadow(boolean castShadow) {
		this.castShadow = castShadow;
	}
	
	public void setColor(float r, float g, float b) {
		if(r > 1 || g > 1 || b > 1) {
			this.color.x = r / 255f;
			this.color.y = g / 255f;
			this.color.z = b / 255f;
		}else {
			this.color.x = r;
			this.color.y = g;
			this.color.z = b;
		}
		if(gameObject != null) {
			this.gameObject.setDirty(true);
		}
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
	public boolean hasTexture() {
		return texture != DEFAULT_TEXTURE;
	}
}
