package com.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
	private static KeyListener instance;
	private boolean keyPressed[] = new boolean[350];
	
	public static KeyListener get() {
		if(KeyListener.instance == null) {
			KeyListener.instance = new KeyListener();
		}
		return KeyListener.instance;
	}
	
	/*
	 * GLSL poll event method for keys
	 * 
	 * @param window		Current GLFW window
	 * @param key			Key ID
	 * @param scancode		Key scan code
	 * @param action		Key action (released / pressed)
	 * @param mods			Simultaneous key presses
	 */
	public static void keyCallback(long window, int key, int scancode, int action, int mods) {
		if(action == GLFW_PRESS) {
			get().keyPressed[key] = true;
		}else if(action == GLFW_RELEASE) {
			get().keyPressed[key] = false;
		}
	}
	
	/*
	 * Checks if key is pressed
	 * 
	 * @param key			Key code to check
	 * @return				If key is pressed
	 */
	public static boolean isKeyPressed(int key) {
		if(key < get().keyPressed.length) {
			return get().keyPressed[key];
		}
		return false;
	}
}
