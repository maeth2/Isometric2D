package com.particles;

import org.joml.Vector2f;

import com.utils.AssetManager;
import com.components.TextureComponent;
import com.utils.Texture;

public class TextParticle extends Particle {
	private static final float PIXEL_WIDTH = 3f; 
	private static final float PIXEL_HEIGHT = 8f; 
	
	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/character.png");
	
	public TextParticle(Vector2f position, float size, float duration) {
		super(position, new Vector2f(PIXEL_WIDTH * size, PIXEL_HEIGHT * size), new TextureComponent(spriteSheet, new Vector2f(3, 8)), duration);
	}
}
