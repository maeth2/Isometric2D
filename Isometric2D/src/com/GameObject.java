package com;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import com.components.Component;
import com.states.StateMachine;
import com.utils.Transform;


public class GameObject {
	private String name;
	private Vector2i gridPosition;
	private boolean isDirty = false;
	private int layer = 0;
	
	protected List<Component> components = new ArrayList<Component>();
	protected List<StateMachine<?, ?, ?>> stateMachines = new ArrayList<StateMachine<?, ?, ?>>();

	public Transform transform;
	
	/**
	 * Initialize game object
	 * 
	 * @param name			Name of object
	 */
	public GameObject(String name) {
		init(name, new Transform(), 1);
	}
	
	/**
	 * Initialize game object
	 * 
	 * @param name			Name of object
	 * @param transform		Transform data of object
	 * @param layer			Layer of object
	 */
	public GameObject(String name, Transform transform) {
		init(name, transform, 1);
	}
	
	/**
	 * Initialize game object
	 * 
	 * @param name			Name of object
	 * @param transform		Transform data of object
	 * @param layer			Layer of object
	 */
	public GameObject(String name, Transform transform, int layer) {
		init(name, transform, layer);
	}
	
	/**
	 * Initialize function of object
	 * 
	 * @param name			Name of object
	 * @param transform		Transform data of object
	 * @param zIndex		Z index of object
	 */
	public void init(String name, Transform transform, int layer) {
		this.name = name;
		this.transform = transform;
		this.layer = layer;
		this.gridPosition = new Vector2i(0, 0);
	}
		
	/**
	 * Get component attached to object
	 * 
	 * @param componentClass			Attached Component to check
	 * @return							Instance of component
	 */
	public <T extends Component> T getComponent(Class<T> componentClass) {
		for(Component c : components) {
			if(componentClass.isAssignableFrom(c.getClass())) {
				return componentClass.cast(c);
			}
		}
		return null;
	}
	
	/**
	 * Remove component from object
	 * 
	 * @param componentClass			Component to detatch
	 * @return							Detachment success
	 */
	public <T extends Component> boolean removeComponent(Class<T> componentClass) {
		for(int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if(componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add component to object
	 * 
	 * @param c			Component to add
	 */
	public GameObject addComponent(Component c) {
		components.add(c);
		c.gameObject = this;
		return this;
	}
	
	/**
	 * Get component attached to object
	 * 
	 * @param componentClass			Attached Component to check
	 * @return							Instance of component
	 */
	public <T extends StateMachine<?, ?, ?>> T getStateMachine(Class<T> stateMachineClass) {
		for(StateMachine<?, ?, ?> c : stateMachines) {
			if(stateMachineClass.isAssignableFrom(c.getClass())) {
				return stateMachineClass.cast(c);
			}
		}
		return null;
	}
	
	/**
	 * Add state machine
	 * 
	 * @param s			State Machine to add
	 */
	public StateMachine<?, ?, ?> addStateMachine(StateMachine<?, ?, ?> s) {
		stateMachines.add(s);
		return s;
	}
	
	/**
	 * Remove state machine from object
	 * 
	 * @param stateClass				Component to detatch
	 * @return							Detachment success
	 */
	public <T extends StateMachine<?, ?, ?>> boolean removeStateMachine(Class<T> stateClass) {
		for(int i = 0; i < stateMachines.size(); i++) {
			StateMachine<?, ?, ?> s = stateMachines.get(i);
			if(stateClass.isAssignableFrom(s.getClass())) {
				stateMachines.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Update funtion
	 * 
	 * @param dt		Delta time
	 */
	public void update(float dt) {
		updateStateMachines(dt);
		updateComponents(dt);
	}
	
	/**
	 * Update Components
	 * 
	 * @param dt		Delta time
	 */
	public void updateComponents(float dt) {
		for(Component c : components) {
			c.update(dt);
		}
	}
	
	
	/**
	 * Update state machines
	 * 
	 * @param dt		Delta time
	 */
	public void updateStateMachines(float dt) {
		for(StateMachine<?, ?, ?> s : stateMachines) {
			s.update(dt);
		}
	}
	
	/**
	 * Start function
	 */
	public void start() {
		for(Component c : components) {
			c.start();
		}
		for(StateMachine<?, ?, ?> s : stateMachines) {
			s.start();
		}
	}
	
	/**
	 * Get Grid Position of Object
	 * 
	 * @return Grid Position of Object
	 */
	public Vector2i getGridPosition() {
		return this.gridPosition;
	}
	
	/**
	 * Set Grid Position of Object
	 * 
	 * @param x			Column of Object
	 * @param y			Row of Object
	 */
	public void setGridPosition(int x, int y) {
		this.gridPosition.x = x;
		this.gridPosition.y = y;
	}
	
	/**
	 * Set Grid Position of Object
	 * 
	 * @param x			Column of Object
	 * @param y			Row of Object
	 * @param i			Index of Object in Grid
	 */
	public void setGridPosition(Vector2i pos) {
		this.gridPosition.x = pos.x;
		this.gridPosition.y = pos.y;
	}

	/**
	 * Get name of object
	 * @return			Name of object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get if game object is dirty
	 * 
	 * @return		Is dirty
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Set if game object is dirty
	 * 
	 * @param isDirty
	 */
	public void setDirty(boolean isDirty) {
		if(!this.isDirty && isDirty) {
			Main.getScene().addDirtyObjects(this);
		}
		this.isDirty = isDirty;
	}
	
	/**
	 * Update layer of object
	 */
	public void setLayer(int layer) {
		if(this.layer != layer) {
			Main.getScene().getRenderer().updateGameObjectLayer(this, layer);
		}
	}
	
	/**
	 * Set layer of object
	 * @param layer
	 */
	public void updateLayer(int layer) {
		this.layer = layer;
	}
	
	/**
	 * Get layer of object
	 * 
	 * @return		Layer of object
	 */
	public int getLayer() {
		return this.layer;
	}
	
	/**
	 * Change position of object
	 * @param dx
	 * @param dy
	 */
	public void changePosition(float dx, float dy) {
		transform.position.x += dx;
		transform.position.y += dy;
		Main.getScene().updateGrid(this);
		setDirty(true);
	}
	
	/**
	 * Set position of object
	 * @param x
	 * @param y
	 */
	public void setPosition(float x, float y) {
		transform.position.x = x;
		transform.position.y = y;
		Main.getScene().updateGrid(this);
		setDirty(true);
	}
	
	/**
	 * Set x position of object
	 * @param x
	 */
	public void setX(float x) {
		transform.position.x = x;
		Main.getScene().updateGrid(this);
		setDirty(true);
	}
		
	/**
	 * Set y position of object
	 * @param y
	 */
	public void setY(float y) {
		transform.position.y = y;
		Main.getScene().updateGrid(this);
		setDirty(true);
	}
	
	/**
	 * Remove game object from scene
	 * 
	 * @param canRemove
	 */
	public void kill() {
		Main.getScene().addDeletableObjects(this);
	}
}
