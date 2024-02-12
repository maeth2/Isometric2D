package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Main;
import com.Window;
import com.components.AABB;
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
		{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 0, 1, 0, 0, 0, 0, 1, 0, 1},
		{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
	};
	
	Random rand;
	
	float cooldown = 0.5f;

	public TestScene(int width, int height) {
		super(width, height);
	}
	
	public void init() {
		rand = new Random();
				
		Main.getRenderer().addComponent(new ShadowShaderComponent(Window.WIDTH, Window.HEIGHT));
		Main.getRenderer().addComponent(new LightShaderComponent(Window.WIDTH, Window.HEIGHT));
		
		camera = new Camera(new Vector2f(0.5f, 0.5f));
		this.addGameObjectToScene(camera);
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
					GameObject block = new GameObject( 
							"test",
							new Transform(
									new Vector2f(j * unitSize, i * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(0f, 0f)
							)
						);
					if(level[i][j] == 1) {
						block.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/tile.png"), true));
						block.addComponent(new AABB(new Vector2f(0, 0), new Vector2f(unitSize / 2, unitSize / 2)));
					}else {
						block.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/floor.png")));
					}
					this.addGameObjectToScene(block);
			}
		}
		
		GameObject player;
		
		player = new GameObject(
				"Martha",
				new Transform(
						new Vector2f(5 * unitSize, 5 * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(180f, 0f)
				)
			);
		player.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/arhum.png")));
		player.addComponent(new ControllerComponent());
		player.addComponent(new AABB(new Vector2f(0, -unitSize / 4), new Vector2f(unitSize / 4, unitSize / 4)));
		player.addComponent(new LightComponent(new Vector3f(1f, 1f, 1f), 400, 1f, true));
		if(Main.getRenderer().getComponent(LightShaderComponent.class) != null) {
			Main.getRenderer().getComponent(LightShaderComponent.class).addLight(player);
		}
		camera.setTarget(player);
		this.addGameObjectToScene(player);
	}
	
	@Override
	public void update(float dt) {
		if(cooldown > 0) {
			cooldown -= dt;
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
	}

}
