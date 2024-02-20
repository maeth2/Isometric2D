package com.entities;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import com.components.AABBComponent;
import com.components.AnimationComponent;
import com.components.ControllerComponent;
import com.components.TextureComponent;
import com.scenes.Scene;
import com.utils.AnimationFrames;
import com.utils.AssetManager;
import com.utils.Texture;
import com.utils.Transform;

public class Jaidyn extends Entity {
	public static Map<String, AnimationFrames> animations = new HashMap<String, AnimationFrames>();
	
	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/character.png");
	static {
		animations.put("Idle", new AnimationFrames(0, 4, 3, 0.5f));
		animations.put("Running", new AnimationFrames(0, 5, 4, 0.5f));
		animations.put("Rolling", new AnimationFrames(0, 4, 5, 0.4f, 1, 4));
	}
	
	public Jaidyn(Transform transform) {
		super("Jaidyn", transform);
		if(transform != null) {
			addComponents();
		}
	}
	
	@Override
	public void update(float dt) {
		updateComponents(dt);
		updateStateMachines(dt);
	}

	@Override
	public void addComponents() {
		addComponent(new TextureComponent(spriteSheet, false, new Vector2f(16, 16)));
		addComponent(new AnimationComponent(animations, "Idle"));
		addComponent(new ControllerComponent(this));
		addComponent(new AABBComponent(new Vector2f(0, -Scene.UNIT_SIZE / 4.5f), new Vector2f(Scene.UNIT_SIZE / 3.5f, Scene.UNIT_SIZE / 4f)));
	}

	@Override
	public Entity create(Transform t) {
		return new Jaidyn(t);
	}
}
