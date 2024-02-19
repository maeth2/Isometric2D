package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Window;
import com.components.AABBComponent;
import com.components.ControllerComponent;
import com.components.LightComponent;
import com.components.TextureComponent;
import com.components.shaders.LightShaderComponent;
import com.components.shaders.ShadowShaderComponent;

import listeners.KeyListener;
import util.AssetManager;
import util.LevelLoader;
import util.Transform;

public class TestScene extends Scene {

	int[][] level= {
		{2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 2, 1, 1, 1, 1, 2, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
		{2, 1, 1, 1, 1, 1, 1, 1, 1, 2},
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
		
		this.setLevel(LevelLoader.loadLevel(10, 10, unitSize, level));
		
//		for(int i = 0; i < 100; i++) {
//			GameObject test = new GameObject( 
//					"Jaidyn",
//					new Transform(
//							new Vector2f(rand.nextFloat() * 50 * unitSize, rand.nextFloat() * 50 * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(0f, 0f)
//					)
//				);
//			test.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/jaidyn.png"), true));
//			this.addGameObjectToScene(test);
//		}
		
		GameObject player;
		
		player = new GameObject( 
				"Jaidyn",
				new Transform(
						new Vector2f(5 * unitSize, 5 * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(0f, 0f)
				)
			);
		player.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/jaidyn.png"), false));
		player.addComponent(new ControllerComponent());
		player.addComponent(new AABBComponent(new Vector2f(0, -unitSize / 4.5f), new Vector2f(unitSize / 3.5f, unitSize / 4f)));
		player.addComponent(new LightComponent(new Vector3f(1f, 1f, 1f), 400f, 1f, true));
		renderer.getComponent(LightShaderComponent.class).addLight(player);
		camera.setTarget(player);
		this.addGameObjectToScene(player);
	}
	
	@Override
	public void update(float dt) {
		if(cooldown > 0) {
			cooldown -= dt;
		}
		
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			if(cooldown < 0) {
				GameObject light = new GameObject(
					"Light",
					new Transform(
							new Vector2f(camera.transform.position.x, camera.transform.position.y), new Vector2f(unitSize / 2, unitSize / 2)
					)
				);
				light.addComponent(new LightComponent(new Vector3f(1f, 0.6f, 0.6f), 800, 1f, true));
				if(renderer.getComponent(LightShaderComponent.class) != null) {
					renderer.getComponent(LightShaderComponent.class).addLight(light);
				}
				this.addGameObjectToScene(light);
				cooldown = 0.5f;
			}
		}
		
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
