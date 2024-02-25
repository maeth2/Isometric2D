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
		this.addGameObjectToScene(camera);
		
		this.setLevel(LevelLoader.loadLevel(10, 10, level));
				
		Entity brynn = EntityList.get("Brynn").create(
			"Brynn",
			new Transform(
				new Vector2f(4.5f * UNIT_SIZE, 8.5f * UNIT_SIZE), 
				new Vector2f(UNIT_SIZE, UNIT_SIZE), 
				new Vector2f(0f, 0f)
			)
		);
		brynn.addComponent(new ControllerComponent(brynn));
		brynn.addComponent(new LightComponent(new Vector3f(100, 100, 100), 400, 1f, true));
		if(renderer.getComponent(LightShaderComponent.class) != null) {
			renderer.getComponent(LightShaderComponent.class).addLight(brynn);
		}
		camera.setTarget(brynn);
		this.addGameObjectToScene(brynn);
		
		Entity jaidyn = EntityList.get("Jaidyn").create(
				"Jaidyn",
				new Transform(
					new Vector2f(4.5f * UNIT_SIZE, 3.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE, UNIT_SIZE), 
					new Vector2f(0f, 0f)
				)
			);
		jaidyn.addComponent(new LightComponent(new Vector3f(100, 100, 100), 400, 1f, true));
		if(renderer.getComponent(LightShaderComponent.class) != null) {
			renderer.getComponent(LightShaderComponent.class).addLight(jaidyn);
		}
		this.addGameObjectToScene(jaidyn);
		
		GameObject weapon = ItemList.get("Syringe").create(
				"Syringe",
				new Transform(
					new Vector2f(2.5f * UNIT_SIZE, 3.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE / 1.5f, UNIT_SIZE / 1.5f), 
					new Vector2f(0f, 0f)
				)
			);
		this.addGameObjectToScene(weapon);
		
		weapon = ItemList.get("Special_stick").create(
				"Syringe",
				new Transform(
					new Vector2f(2.5f * UNIT_SIZE, 8.5f * UNIT_SIZE), 
					new Vector2f(UNIT_SIZE / 1.5f, UNIT_SIZE / 1.5f), 
					new Vector2f(0f, 0f)
				)
			);
		this.addGameObjectToScene(weapon);
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
				this.addGameObjectToScene(light);
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
