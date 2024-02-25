package com.entities.players;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import com.components.AABBComponent;
import com.components.AnimationComponent;
import com.components.TextureComponent;
import com.entities.Entity;
import com.scenes.Scene;
import com.utils.Animation;
import com.utils.AssetManager;
import com.utils.Texture;
import com.utils.Transform;

public class Brynn extends Entity {
	public static Map<String, Animation> animations = new HashMap<String, Animation>();
	
	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/character.png");
	static {
		animations.put("Idle", Animation.createAnimationFrame(0, 4, 6, 0.5f));
		animations.put("Running", Animation.createAnimationFrame(0, 5, 7, 0.5f));
		animations.put("Rolling", Animation.createAnimationFrame(0, 4, 8, 0.35f, 0, 4));
	}
	
	public Brynn(String name, Transform transform) {
		super(name, transform);
	}

	@Override
	public void addComponents() {
		addComponent(new TextureComponent(spriteSheet, false, new Vector2f(16, 16)));
		addComponent(new AnimationComponent(animations, "Idle"));
		addComponent(new AABBComponent(this, new Vector2f(0, 0), new Vector2f(Scene.UNIT_SIZE * 0.8f, Scene.UNIT_SIZE)));
	}

	@Override
	public Entity create(String name, Transform t) {
		return new Brynn(name, t);
	}
}
