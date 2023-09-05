package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Main;
import com.Window;
import com.components.ControllerComponent;
import com.components.LightComponent;
import com.components.TextureComponent;
import com.components.shaders.LightShaderComponent;
import com.components.shaders.ShadowShaderComponent;

import listeners.KeyListener;
import util.AssetManager;
import util.Transform;

public class TestScene extends Scene {
	int[][] level= {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	Random rand;
	
	int unitSize = 50;
	
	float cooldown = 0.5f;
	
	public void init() {
		rand = new Random();
		camera = new Camera(new Vector2f(0, 0));
		this.addGameObjectToScene(camera);
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
					GameObject block = new GameObject(
							"test",
							new Transform(
									new Vector2f((j - 5) * unitSize * 2, (i - 5) * unitSize * 2), new Vector2f(unitSize, unitSize), new Vector2f(0f, 0f)
							)
						);
					if(level[i][j] == 1) {
						block.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/tile.png"), true));
					}else {
						block.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/floor.png")));
					}
					block.addComponent(new ControllerComponent());
					this.addGameObjectToScene(block);
			
			}
		}
		GameObject test;
		for(int i = 0; i < 10; i++) {
			test = new GameObject(
					"test",
					new Transform(
							new Vector2f(rand.nextFloat() * 1000 - 500, rand.nextFloat() * 1000 - 500), new Vector2f(unitSize, unitSize), new Vector2f(0f, 0f)
					)
				);
			test.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/tile.png"), true));
			this.addGameObjectToScene(test);
		}
		
		test = new GameObject(
				"test",
				new Transform(
						new Vector2f(0.5f, 0.5f), new Vector2f(unitSize, unitSize), new Vector2f(0f, 0f)
				)
			);
		test.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/blank.png")));
		test.addComponent(new ControllerComponent());
		this.addGameObjectToScene(test);
		camera.setTarget(test);
		
		Main.getRenderer().addComponent(new ShadowShaderComponent(Window.WIDTH, Window.HEIGHT));
		Main.getRenderer().addComponent(new LightShaderComponent(Window.WIDTH, Window.HEIGHT));
		
		GameObject light = new GameObject("light");
		light.addComponent(new LightComponent(new Vector3f(0, 0, 1f), 400, true));
		light.transform.position = camera.transform.position;
		Main.getRenderer().getComponent(LightShaderComponent.class).addLight(light);
		this.addGameObjectToScene(light);
	}
	
	@Override
	public void update(float dt) {
		if(cooldown > 0) {
			cooldown -= dt;
		}
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			if(cooldown < 0) {
				GameObject light = new GameObject("light");
				light.addComponent(new LightComponent(new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()), 800, 0.7f, true));
				light.transform.position.x = getCamera().getTarget().transform.position.x;
				light.transform.position.y = getCamera().getTarget().transform.position.y;
				Main.getRenderer().getComponent(LightShaderComponent.class).addLight(light);
				this.addGameObjectToScene(light);
				cooldown =  0.5f;
			}
			//camera.changeZoom(0.001f);
		}
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			//camera.changeZoom(-0.001f);
		}
		for(GameObject o : gameObjects) {
			o.update(dt);
		}
	}

}
