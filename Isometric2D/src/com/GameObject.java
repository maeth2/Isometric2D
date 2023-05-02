package com;

import java.util.ArrayList;
import java.util.List;

import com.components.Component;

import util.Transform;


public class GameObject {
	private String name;
	private List<Component> components = new ArrayList<Component>();
	public Transform transform;
	
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
	 */
	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for(int i = 0; i < components.size(); i++) {
			Component c = components.get(i);
			if(componentClass.isAssignableFrom(c.getClass())) {
				components.remove(i);
				return;
			}
		}
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
	 * Update funtion
	 * 
	 * @param dt		Delta time
	 */
	public void update(float dt) {
		for(Component c : components) {
			c.update(dt);
		}
	}
	
	/**
	 * Start function
	 */
	public void start() {
		for(Component c : components) {
			c.start();
		}
	}
	
	/**
	 * Get name of object
	 * @return			Name of object
	 */
	public String getName() {
		return name;
	}
	
}
