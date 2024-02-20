package com;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.joml.Vector3i;

import com.components.Component;
import com.states.StateMachine;
import com.utils.Transform;


public class GameObject {
	private String name;
	protected List<Component> components = new ArrayList<Component>();
	protected List<StateMachine<?>> stateMachines = new ArrayList<StateMachine<?>>();
	public Transform transform;
	private Vector3i gridPosition;
	private boolean isDirty = false;
	
	/**
	 * Initialize game object
	 * 
	 * @param name			Name of object
	 */
	public GameObject(String name) {
		init(name, new Transform());
	}
	
	/**
	 * Initialize game object
	 * 
	 * @param name			Name of object
	 * @param transform		Transform data of object
	 * @param zIndex		Z index of object
	 */
	public GameObject(String name, Transform transform) {
		init(name, transform);
	}
	
	/**
	 * Initialize function of object
	 * 
	 * @param name			Name of object
	 * @param transform		Transform data of object
	 * @param zIndex		Z index of object
	 */
	public void init(String name, Transform transform) {
		this.name = name;
		this.transform = transform;
		this.gridPosition = new Vector3i(0, 0, 0);
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
	 * Remove state machine from object
	 * 
	 * @param stateClass				Component to detatch
	 * @return							Detachment success
	 */
	public <T extends StateMachine<?>> boolean removeStateMachine(Class<T> stateClass) {
		for(int i = 0; i < stateMachines.size(); i++) {
			StateMachine<?> s = stateMachines.get(i);
			if(stateClass.isAssignableFrom(s.getClass())) {
				stateMachines.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add state machine
	 * 
	 * @param s			State Machine to add
	 */
	public StateMachine<?> addStateMachine(StateMachine<?> s) {
		stateMachines.add(s);
		return s;
	}
	
	/**
	 * Update funtion
	 * 
	 * @param dt		Delta time
	 */
	public void update(float dt) {
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
		for(StateMachine<?> s : stateMachines) {
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
		for(StateMachine<?> s : stateMachines) {
			s.start();
		}
	}
	
	/**
	 * Get Grid Position of Object
	 * 
	 * @return Grid Position of Object
	 */
	public Vector3i getGridPosition() {
		return this.gridPosition;
	}
	
	/**
	 * Set Grid Position of Object
	 * 
	 * @param x			Column of Object
	 * @param y			Row of Object
	 * @param i			Index of Object in Grid
	 */
	public void setGridPosition(int x, int y, int i) {
		this.gridPosition.x = x;
		this.gridPosition.y = y;
		this.gridPosition.z = i;
	}
	
	/**
	 * Set Grid Position of Object
	 * 
	 * @param x			Column of Object
	 * @param y			Row of Object
	 * @param i			Index of Object in Grid
	 */
	public void setGridPosition(Vector2i pos, int i) {
		this.gridPosition.x = pos.x;
		this.gridPosition.y = pos.y;
		this.gridPosition.z = i;
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
		this.isDirty = isDirty;
	}
}
