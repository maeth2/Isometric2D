package com.particles;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.utils.AssetManager;
import com.components.TextureComponent;
import com.utils.Texture;

public class TextParticle{
	private static final float PIXEL_WIDTH = 7f; 
	private static final float PIXEL_HEIGHT = 9f; 
	
	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/letters.png");
	
	private static Vector2f lookupTable[] = new Vector2f[127];
	
	static {
		lookupTable[46] = new Vector2f(0, 7); //.
		lookupTable[44] = new Vector2f(1, 7); //,
		lookupTable[33] = new Vector2f(2, 7); //!
		lookupTable[63] = new Vector2f(3, 7); //?
		lookupTable[58] = new Vector2f(4, 7); //:
		lookupTable[39] = new Vector2f(5, 7); //'
		
		for(int i = 0; i < 10; i++) {
			lookupTable['0' + i] = new Vector2f(i % 6, i / 6);
		}
		
		for(int i = 0; i < 26; i++) {
			lookupTable['A' + i] = new Vector2f(i % 6, i / 6 + 2);
		}
	}
			
	public static List<Particle> createTextParticle(String text, Vector2f position, float size, Vector2f velocity, float duration, Vector3f color) {
		List<Particle> characters = new ArrayList<Particle>();
		float width = size;
		float height = PIXEL_HEIGHT / PIXEL_WIDTH * size;
		float xOffset = width * text.length() / 2f;
		for(int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c == ' ') continue;

			Particle p = new Particle(
					new Vector2f(position.x + width * i - xOffset, position.y), 
					new Vector2f(width, height), 
					new TextureComponent(spriteSheet, lookupTable[c], new Vector2f(PIXEL_WIDTH, PIXEL_HEIGHT)), 
					velocity,
					duration
			);
			p.getTexture().setColor(color.x, color.y, color.z);
			characters.add(p);
		}
		
		return characters;
	}
}
