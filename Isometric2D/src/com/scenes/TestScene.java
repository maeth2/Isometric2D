package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Window;
import com.components.LightComponent;
import com.components.shaders.LightShaderComponent;
import com.components.shaders.ShadowShaderComponent;
import com.entities.EntityList;
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
				
		GameObject player = EntityList.get("Jaidyn").create(
			new Transform(
				new Vector2f(5 * UNIT_SIZE, 5 * UNIT_SIZE), 
				new Vector2f(UNIT_SIZE / 2, UNIT_SIZE / 2), 
				new Vector2f(0f, 0f)
			)
		);
		this.addGameObjectToScene(player);

		camera.setTarget(player);
	}
	
	@Override
	public void update(float dt) {
		if(cooldown > 0) {
			cooldown -= dt;
		}
		
//		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
//			if(cooldown < 0) {
//				GameObject light = new GameObject(
//					"Light",
//					new Transform(
//							new Vector2f(camera.transform.position.x, camera.transform.position.y), new Vector2f(UNIT_SIZE / 2, UNIT_SIZE / 2)
//					)
//				);
//				light.addComponent(new LightComponent(new Vector3f(1f, 0.6f, 0.6f), 800, 1f, true));
//				if(renderer.getComponent(LightShaderComponent.class) != null) {
//					renderer.getComponent(LightShaderComponent.class).addLight(light);
//				}
//				this.addGameObjectToScene(light);
//				cooldown = 0.5f;
//			}
//		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_V)) {
			if(cooldown < 0) {
				camera.toggleFreeCam();
				cooldown = 0.5f;
			}
		}

		for(GameObject o : gameObjects) {
			o.update(dt);
		}
		
		for(GameObject l : this.getSurroundingLevel(this.getCamera(), 10, 10)) {
			l.update(dt);
		}
		
		renderer.render();
	}

}
