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

public class Jaidyn extends Entity {
	public static Map<Enum<?>, Animation> animations = new HashMap<Enum<?>, Animation>();

	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/character.png");
	static {
		animations.put(states.Idle, Animation.createAnimationFrame(0, 4, 3, 0.5f));
		animations.put(states.Running, Animation.createAnimationFrame(0, 5, 4, 0.5f));
		animations.put(states.Rolling, Animation.createAnimationFrame(0, 4, 5, 0.3f, 0, 4));
	}
	
	public Jaidyn(String name, Transform transform) {
		super(name, transform);
	}

	@Override
	public void addComponents() {
		addComponent(new TextureComponent(spriteSheet, false, new Vector2f(16, 16)));
		addComponent(new AnimationComponent(animations, states.Idle));
		addComponent(new AABBComponent(this, new Vector2f(0, 0), new Vector2f(Scene.UNIT_SIZE * 0.8f, Scene.UNIT_SIZE)));
	}

	@Override
	public Entity create(String name, Transform t) {
		return new Jaidyn(name, t);
	}
}