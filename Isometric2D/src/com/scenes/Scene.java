package com.scenes;

import java.util.ArrayList;

import java.util.List;

import com.Camera;
import com.GameObject;

public abstract class Scene {	
	public static int MAX_SCENE_LIGHTS = 10;
	protected Camera camera;
	protected float unitSize = 100f;
	private boolean isRunning = false;
	protected List<GameObject> grid[][];
	protected int gridWidth, gridHeight;
	protected List<GameObject> gameObjects = new ArrayList<GameObject>();
	
	public Scene(int width, int height) {
		this.gridWidth = width;
		this.gridHeight = height;
		this.initialiseGrid(width, height);
	}
	
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
	public void addGameObjectToScene(GameObject o) {
		gameObjects.add(o);
		if(isRunning) {
			o.start();
		}
		int r = (int)(o.transform.position.y / unitSize);
		int c = (int)(o.transform.position.x / unitSize);
		if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) return;
		grid[r][c].add(o);
		o.setGridPosition(c, r, grid[r][c].size() - 1);
	}
	
	/**
	 * Start function
	 */
	public void start() {
		for(GameObject o : gameObjects) {
			o.start();
		}

		isRunning = true;
	}
	
	/**
	 * Get List of Game Objects in the Scene
	 * 
	 * @return GameObject List
	 */
	public List<GameObject> getGameObjects(){
		return gameObjects;
	}
	
	/**
	 * Get Grid of Game Objects in the Scene
	 * 
	 * @return GameObject Grid
	 */
	public List<GameObject>[][] getGrid() {
		return this.grid;
	}
	
	/**
	 * Update Game Object Grid
	 */
	public void updateGrid(GameObject o){
		int r = (int)(o.transform.position.y / unitSize);
		int c = (int)(o.transform.position.x / unitSize);
		if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) return;
		int r1 = o.getGridPosition().y;
		int c1 = o.getGridPosition().x;
		int i = o.getGridPosition().z;
		if(r != r1 || c != c1) {
			grid[r1][c1].remove(i);
			grid[r][c].add(o);
			o.setGridPosition(c, r, grid[r][c].size() - 1);
		}
	}
	
	/**
	 * Get Surrounding GameObjects Based on GameObject Grid Position
	 * 
	 * @param o 		GameObject to Check
	 * @return			List of Surrounding GameObjects
	 */
	public List<GameObject> getSurroundingGridObjects(GameObject o) {
		List<GameObject> go = new ArrayList<GameObject>();
		for(int r1 = -1; r1 <= 1; r1++) {
			for(int c1 = -1; c1 <= 1; c1++) {
				int r = o.getGridPosition().y + r1;
				int c = o.getGridPosition().x + c1;
				if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) continue;
				for(GameObject i : this.grid[r][c]) {
					go.add(i);
				}
			}
		}
		return go;
	}
	
	/**
	 * Initialise the Grid
	 * 
	 * @param width 	Width of Grid
	 * @param height	Hiehgt of grid 
	 */
	@SuppressWarnings("unchecked")
	protected void initialiseGrid(int width, int height) {
		this.grid = new List[height][width]; 
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				this.grid[i][j] = new ArrayList<GameObject>();
			}
		}
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
