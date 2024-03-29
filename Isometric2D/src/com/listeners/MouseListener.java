package com.listeners;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import com.Window;


public class MouseListener {
	public static MouseListener instance;
	private double scrollX, scrollY;
	private double xPos, yPos, lastY, lastX;
	private boolean mouseButtonPressed[] = new boolean[3];
	private boolean isDragging;
	
	private MouseListener() {
		this.scrollX = 0.0;
		this.scrollY = 0.0;
		this.xPos = 0.0;
		this.yPos = 0.0;
		this.lastX = 0.0;
		this.lastY = 0.0;
	}
	
	/**
	 * Get a static instance of the Mouse Listener
	 * @return
	 */
	public static MouseListener get() {
		if(MouseListener.instance == null) {
			MouseListener.instance = new MouseListener();
		}
		return MouseListener.instance;
	}
	
	/*
	 * GLFW mouse position event function
	 * 
	 * @param window		Current GLFW window
	 * @param xpos			Current mouse x pos
	 * @param ypos			Current mouse y pos
	 */
	public static void mousePosCallback(long window, double xpos, double ypos) {
		get().lastX = get().xPos;
		get().lastY = get().yPos;
		get().xPos = xpos;
		get().yPos = ypos;
		get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
	}
	
	/*
	 * GLFW mouse button event function
	 * 
	 * @param window		Current GLFW window
	 * @param button		Mouse button ID
	 * @param action		Mouse button action
	 * @param mods			Simultaneous key presses
	 */
	public static void mouseButtonCallback(long window, int button, int action, int mods) {
		if(action == GLFW_PRESS) {
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = true;
			}
		}else if(action == GLFW_RELEASE) {
			if(button < get().mouseButtonPressed.length) {
				get().mouseButtonPressed[button] = false;
				get().isDragging = false;
			}
		}
	}
	
	/*
	 * GLFW mouse scroll event function
	 * 
	 * @param window		Current GLFW window
	 * @param xOffset		Scroll wheel x movement
	 * @param yOffset		Scroll wheel y movement
	 */
	public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
		get().scrollX = xOffset;
		get().scrollY = yOffset;
	}
	
	public static void endFrame() {
		get().scrollX = 0;
		get().scrollY = 0;
		get().lastX = get().xPos;
		get().lastY = get().yPos;
	}
	
	public static float getX() {
		return (float)get().xPos - (float) (Window.WIDTH / 2.0f);
	}
	
	public static float getY() {
		return (float) (Window.HEIGHT / 2.0f) - (float)get().yPos;
	}
	
	public static float getDx() {
		return (float)(get().lastX - get().xPos);
	}
	
	public static float getDy() {
		return (float)(get().lastY - get().yPos);
	}
	
	public static float getScrollX() {
		return (float)get().scrollX;
	}
	
	public static float getScrollY() {
		return (float)get().scrollY;
	}
	
	public static boolean isDragging() {
		return get().isDragging;
	}
	
	public static boolean mouseButtonDown(int button) {
		if(button < get().mouseButtonPressed.length) {
			return get().mouseButtonPressed[button];
		}
		return false;
	}
}
