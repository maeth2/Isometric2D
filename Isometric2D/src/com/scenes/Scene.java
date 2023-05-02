package com.scenes;

import java.util.ArrayList;
import java.util.List;

import com.Camera;
import com.GameObject;

public abstract class Scene {	
	public static int MAX_SCENE_LIGHTS = 10;
	protected Camera camera;
	private boolean isRunning = false;
	protected List<GameObject> gameObjects = new ArrayList<GameObject>();
	
	public Scene() {}
	
	/**
	 * Initializes the scene
	 */
	public void init() {}
	
	/**
	 * Updates the scene
	 * 
	 * @param dt		Time passed per frame
	 */
	public abstract void update(float dt);
	
	/**
	 * Adds a game object to the scene
	 * 
	 * @param g			Game object to add
	 */
	public void addGameObjectToScene(GameObject g) {
		gameObjects.add(g);
		if(isRunning) {
			g.start();
		}
	}
	
	/**
	 * Start function
	 */
	public void start() {
		for(GameObject g : gameObjects) {
			g.start();
		}

		isRunning = true;
	}
	
	public List<GameObject> getGameObjects(){
		return gameObjects;
	}
	
	/**
	 * Get scene camera
	 * 
	 * @return			Scene camera
	 */
	public Camera getCamera() {
		return this.camera;
	}
}
