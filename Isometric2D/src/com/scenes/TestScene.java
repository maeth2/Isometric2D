package com.scenes;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import com.Camera;
import com.GameObject;
import com.components.ControllerComponent;
import com.components.TextureComponent;

import listeners.KeyListener;
import util.AssetManager;
import util.Transform;

public class TestScene extends Scene {
	public TestScene() {
		init();
	}
	
	public void init() {
		camera = new Camera(new Vector2f(0, 0));
		this.addGameObjectToScene(camera);
		
		GameObject test = new GameObject(
				"test",
				new Transform(
						new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f), new Vector2f(45f, 0f)
				)
			);
		test.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/blank.png")));
		test.addComponent(new ControllerComponent());
		this.addGameObjectToScene(test);
		
		camera.setTarget(test);
		
		GameObject test2 = new GameObject(
				"test2",
				new Transform(
						new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f), new Vector2f(45f, 0f)
				)
			);
		test2.addComponent(new TextureComponent(AssetManager.getTexture("assets/textures/mario.png")));
		this.addGameObjectToScene(test2);
	}
	
	@Override
	public void update(float dt) {
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			camera.changeZoom(1);
		}
		if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			camera.changeZoom(-1);
		}
		for(GameObject o : gameObjects) {
			o.update(dt);
		}
	}

}
