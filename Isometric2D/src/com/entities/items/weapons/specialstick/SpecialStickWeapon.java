package com.entities.items.weapons.specialstick;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector2f;

import com.components.AABBComponent;
import com.components.AnimationComponent;
import com.components.TextureComponent;
import com.entities.items.Item;
import com.scenes.Scene;
import com.utils.Animation;
import com.utils.AssetManager;
import com.utils.Texture;
import com.utils.Transform;

public class SpecialStickWeapon extends Item {
	public static Map<String, Animation> animations = new HashMap<String, Animation>();

	public static Texture spriteSheet = AssetManager.getTexture("assets/textures/weapons.png");
	static {
		animations.put("Idle", Animation.createAnimationFrame(0, 0, 2, 1f));
		animations.put("Picked", Animation.createAnimationFrame(0, 0, 2, 0f));
	}
	
	public SpecialStickWeapon(String name, Transform transform) {
		super(name, transform, new SpecialStickAttackState(null, null));
	}
	
	@Override
	public void update(float dt) {
		updateComponents(dt);
		updateStateMachines(dt);
	}

	@Override
	public void addComponents() {
		addComponent(new TextureComponent(spriteSheet, false, new Vector2f(1, 1), new Vector2f(16, 16)));	
		addComponent(new AnimationComponent(animations, "Idle"));
		addComponent(new AABBComponent(this, new Vector2f(0, 0), new Vector2f(Scene.UNIT_SIZE * 0.4f, Scene.UNIT_SIZE * 0.8f)));
	}

	@Override
	public Item create(String name, Transform transform) {
		return new SpecialStickWeapon(name, transform);
	}
}
