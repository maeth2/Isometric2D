package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Window;
import com.components.ControllerComponent;
import com.components.LightComponent;
import com.components.shaders.LightShaderComponent;
import com.components.shaders.ShadowShaderComponent;
import com.entities.Entity;
import com.entities.EntityList;
import com.entities.items.ItemList;
import com.listeners.KeyListener;
import com.utils.LevelLoader;
import com.utils.Transform;
import com.states.ai.TestAI;

public class TestScene extends Scene {

	int[][] level= {
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 2, 2, 2, 1, 1, 2},
		{2, 1, 1, 2, 0, 0, 2, 1, 1, 2},
		{2, 1, 1, 2, 2, 2, 2, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 2, 2, 2, 2, 1, 1, 2},
		{2, 1, 1, 2, 0, 0, 2, 1, 1, 2},
		{2, 2, 2, 2, 0, 0, 2, 2, 2, 2}
	};
	
	Random rand;
	
	float cooldown = 0.5f;

	public TestScene(int width, int height) {
		super(width, height);
	}
	
	public void init() {
		rand = new Random();

		renderer.addComponent(new ShadowShaderComponent(Window.WIDTH, Window.HEIGHT));

		camera = new Camera(new Vector2f(0.5f, 0.5f));
		this.addGameObject(camera);
		
		this.setLevel(LevelLoader.loadLevel(10, 10, level));
		
		stressTest(0);
		
		Entity jaidyn = EntityList.get(EntityList.entity.Jaidyn).create(
				"Jaidyn",
				new Transform(
					new Vector2f(4.5f * UNIT_SIZE, 3.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE, UNIT_SIZE) 
				)
			);
		jaidyn.addComponent(new LightComponent(new Vector3f(100, 100, 100), 400, 1f, false));
		jaidyn.addStateMachine(new TestAI(jaidyn));
		if(renderer.getComponent(LightShaderComponent.class) != null) {
			renderer.getComponent(LightShaderComponent.class).addLight(jaidyn);
		}
		jaidyn.setBaseSpeed(UNIT_SIZE);
		this.addGameObject(jaidyn);
		
//		stressTest(100);
				
		Entity brynn = EntityList.get(EntityList.entity.Brynn).create(
				"Brynn",
				new Transform(
					new Vector2f(4.5f * UNIT_SIZE, 8.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE, UNIT_SIZE) 
				)
			);
		brynn.addComponent(new ControllerComponent(brynn));
		brynn.addComponent(new LightComponent(new Vector3f(100, 100, 100), 400, 1f, true));
		if(renderer.getComponent(LightShaderComponent.class) != null) {
			renderer.getComponent(LightShaderComponent.class).addLight(brynn);
		}
		camera.setTarget(brynn);
		this.addGameObject(brynn);
				
		GameObject weapon = ItemList.get(ItemList.item.Syringe).create(
				"Syringe",
				new Transform(
					new Vector2f(2.5f * UNIT_SIZE, 3.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE / 1.5f, UNIT_SIZE / 1.5f) 
				)
			);
		this.addGameObject(weapon);
		
		weapon = ItemList.get(ItemList.item.SpecialStick).create(
				"Stick",
				new Transform(
					new Vector2f(2.5f * UNIT_SIZE, 8.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE / 1.5f, UNIT_SIZE / 1.5f)
				)
			);
		this.addGameObject(weapon);
	}
	
	private void stressTest(int num) {
		for(int i = 0; i < num; i++) {
			Entity jaidyn = EntityList.get(EntityList.entity.Jaidyn).create(
					"Jaidyn",
					new Transform(
						new Vector2f(rand.nextFloat() * 10 * UNIT_SIZE, rand.nextFloat() * 10 * UNIT_SIZE), 
						new Vector2f(UNIT_SIZE, UNIT_SIZE) 
					)
				);
			jaidyn.setBaseSpeed(UNIT_SIZE);
			jaidyn.addStateMachine(new TestAI(jaidyn));
			this.addGameObject(jaidyn);
		}
	}
	
	@Override
	public void updateDebug(float dt) {
		if(cooldown > 0) {
			cooldown -= dt;
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_V)) {
			if(cooldown < 0) {
				GameObject light = new GameObject(
					"Light",
					new Transform(
							new Vector2f(camera.transform.position.x, camera.transform.position.y), new Vector2f(UNIT_SIZE, UNIT_SIZE)
					)
				);
				light.addComponent(new LightComponent(new Vector3f(227, 139, 89), 400, 1f, true));
				if(renderer.getComponent(LightShaderComponent.class) != null) {
					renderer.getComponent(LightShaderComponent.class).addLight(light);
				}
				addGameObject(light);
				cooldown = 0.5f;
			}
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)) {
			if(cooldown < 0) {
				camera.toggleFreeCam();
				cooldown = 0.5f;
			}
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_T)) {
			if(cooldown < 0) {
				renderer.toggleDebug();
				cooldown = 0.5f;
			}
		}
	}

}
