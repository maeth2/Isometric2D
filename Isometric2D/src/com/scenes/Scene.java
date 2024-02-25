package com.scenes;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import com.Camera;
import com.GameObject;
import com.Renderer;

public abstract class Scene {	
	public static int MAX_SCENE_LIGHTS = 10;
	public static float UNIT_SIZE = 100f;

	protected Camera camera;
	private boolean isRunning = false;
	protected int gridWidth, gridHeight;
	protected Renderer renderer;

	protected List<GameObject> gameObjects = new ArrayList<GameObject>();
	protected List<GameObject> dirtyObjects = new ArrayList<GameObject>();
	protected List<GameObject> deleteObjects = new ArrayList<GameObject>();
	protected List<GameObject> grid[][];
	protected GameObject level[][];
	
	private List<GameObject> surroundingLevel = new ArrayList<GameObject>();
	private List<GameObject> surroundingGrid = new ArrayList<GameObject>();

	public Scene(int width, int height) {
		this.renderer = new Renderer();
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
	public void update(float dt) {
		updateDebug(dt);
		for(GameObject o : gameObjects) {
			o.update(dt);
		}
		
		for(GameObject l : this.getSurroundingLevel(this.getCamera(), 10, 10)) {
			l.update(dt);
		}
		
		renderer.render();
		cleanObjects();
	}
	
	/**
	 * Debugger update
	 */
	public abstract void updateDebug(float dt);
	
	/**
	 * Adds a game object to the scene
	 * 
	 * @param g			Game object to add
	 */
	public void addGameObjectToScene(GameObject o) {
		gameObjects.add(o);
		dirtyObjects.add(o);
		renderer.addRenderObject(o);
		
		if(isRunning) {
			o.start();
		}
		
		int r = (int)(o.transform.position.y / UNIT_SIZE);
		int c = (int)(o.transform.position.x / UNIT_SIZE);
		if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) return;
		grid[r][c].add(o);
		o.setGridPosition(c, r);
	}
	
	/**
	 * Removes game object from the scene
	 * 
	 * @param g			Game object to remove
	 */
	public void removeGameObjectFromScene(GameObject o) {
		gameObjects.remove(o);
		renderer.removeRenderObject(o);

		int r = (int)(o.transform.position.y / UNIT_SIZE);
		int c = (int)(o.transform.position.x / UNIT_SIZE);
		if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) return;
		System.out.println("Before: " + grid[r][c].size());
		grid[r][c].remove(o);
		System.out.println("After: " + grid[r][c].size());
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
		return this.gameObjects;
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
	 * Set Level Layout
	 * 
	 * @param x			Column of Level
	 * @param y			Row of Level
	 * @param o			GameObject to add
	 */
	public void setLevel(int x, int y, GameObject o) {
		this.level[y][x] = o;
	}
	
	/**
	 * Set Level Layout
	 * 
	 * @param level		Level Layout
	 */
	public void setLevel(GameObject[][] level) {
		for(GameObject r[] : level) {
			for(GameObject c : r) {
				renderer.addRenderObject(c);
			}
		}
		this.level = level;
	}
	
	/**
	 * Get Level Layout in the Scene
	 * 
	 * @return Level Layout
	 */
	public GameObject[][] getLevel() {
		return this.level;
	}
	
	/**
	 * Update Game Object Grid
	 */
	public void updateGrid(GameObject o){
		Vector2i pos = this.getGridPosition(o);
		if(pos.x < 0 || pos.x >= this.gridWidth || pos.y < 0 || pos.y >= this.gridHeight) return;
		if(pos.x != o.getGridPosition().x || pos.y != o.getGridPosition().y) {
			grid[o.getGridPosition().y][o.getGridPosition().x].remove(o);
			grid[pos.y][pos.x].add(o);
			o.setGridPosition(pos.x, pos.y);
		}
	}
	
	/**
	 * Get Grid Position of Object
	 * 
	 * @param o			Object to Check
	 * @return			Grid Position of Object
	 */
	public Vector2i getGridPosition(GameObject o) {
		return new Vector2i((int)(o.transform.position.x / UNIT_SIZE), (int)(o.transform.position.y / UNIT_SIZE));
	}
	
	/**
	 * Get Surrounding GameObjects Based on GameObject Grid Position
	 * 
	 * @param o 		GameObject to Check
	 * @return			List of Surrounding GameObjects
	 */
	public List<GameObject> getSurroundingGrid(GameObject o, int width, int height) {
		this.surroundingGrid.clear();
		for(int r1 = -width; r1 <= width; r1++) {
			for(int c1 = -height; c1 <= height; c1++) {
				int r = o.getGridPosition().y + r1;
				int c = o.getGridPosition().x + c1;
				if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) continue;
				for(GameObject i : this.grid[r][c]) {
					surroundingGrid.add(i);
				}
			}
		}
		return surroundingGrid;
	}
	
	/**
	 * Get Surrounding GameObjects Based on GameObject Grid Position
	 * 
	 * @param o 		GameObject to Check
	 * @return			List of Surrounding GameObjects
	 */
	public List<GameObject> getSurroundingLevel(GameObject o, int width, int height) {
		this.surroundingLevel.clear();
		for(int r1 = -width; r1 <= width; r1++) {
			for(int c1 = -height; c1 <= height; c1++) {
				int r = o.getGridPosition().y + r1;
				int c = o.getGridPosition().x + c1;
				if(r < 0 || r >= this.gridHeight || c < 0 || c >= this.gridWidth) continue;
				surroundingLevel.add(this.level[r][c]);
			}
		}
		return surroundingLevel;
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
		this.level = new GameObject[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				this.grid[i][j] = new ArrayList<GameObject>();
			}
		}
	}
	
	/**
	 * Add dirty game object to scene
	 */
	public void addDirtyObjects(GameObject o) {
		dirtyObjects.add(o);
	}
	
	/**
	 * Add deletable game object to scene
	 */
	public void addDeletableObjects(GameObject o) {
		deleteObjects.add(o);
	}
	
	/**
	 * Reset all dirty objects to clean
	 */
	public void cleanObjects() {
		for(GameObject i : deleteObjects) {
			this.removeGameObjectFromScene(i);
		}
		for(GameObject i : dirtyObjects) {
			i.setDirty(false);
		}
		dirtyObjects.clear();
		deleteObjects.clear();
	}
	
	/**
	 * Get scene camera
	 * 
	 * @return			Scene camera
	 */
	public Camera getCamera() {
		return this.camera;
	}
	
	/**
	 * Get scene renderer
	 * 
	 * @return			Scene Renderer
	 */
	public Renderer getRenderer() {
		return this.renderer;
	}
}
