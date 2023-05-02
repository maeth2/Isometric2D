package com.components;

import com.GameObject;

public abstract class Component {
	
	public GameObject gameObject;
	
	public abstract void start();
	
	public abstract void update(float dt);

}
