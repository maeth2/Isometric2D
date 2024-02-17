package com.scenes;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.Main;
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

		Main.getRenderer().addComponent(new ShadowShaderComponent(Window.WIDTH, Window.HEIGHT));
		Main.getRenderer().addComponent(new LightShaderComponent(Window.WIDTH, Window.HEIGHT));

		camera = new Camera(new Vector2f(0.5f, 0.5f));
		this.addGameObjectToScene(camera);
		
		this.setLevel(LevelLoader.loadLevel(10, 10, unitSize, level));
		
		GameObject player;
		
		player = new GameObject( 
				"Martha",
				new Transform(
						new Vector2f(5 * unitSize, 5 * unitSize), new Vector2f(unitSize / 2, unitSize / 2), new Vector2f(180f, 0f)
				)
			);
		player.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/arhum.png"), false));
		player.addComponent(new ControllerComponent());
		player.addComponent(new AABBComponent(new Vector2f(0, -unitSize / 4.5f), new Vector2f(unitSize / 4, unitSize / 3.5f)));
		player.addComponent(new LightComponent(new Vector3f(1f, 1f, 1f), 400f, 1f, true));
		Main.getRenderer().getComponent(LightShaderComponent.class).addLight(player);
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
				if(Main.getRenderer().getComponent(LightShaderComponent.class) != null) {
					Main.getRenderer().getComponent(LightShaderComponent.class).addLight(light);
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
	}

}
