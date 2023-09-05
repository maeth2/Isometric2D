package com;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;

import listeners.KeyListener;
import listeners.MouseListener;

public class Window {
	public long window;
	private static Window instance;
	private long glfwWindow;
	public static int WIDTH, HEIGHT;
	private String name;
	
	
	public Window() {
		this.name = "OTHS";
		WIDTH = 1920;
		HEIGHT = 1000;
	}
	
	public void init() {
		System.out.println("CREATING WINDOW...");

		GLFWErrorCallback.createPrint(System.err).set();
				
		if(!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		//glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
		
		glfwWindow = glfwCreateWindow(WIDTH, HEIGHT, this.name, 0, 0);
		
		if(glfwWindow == 0) {
			throw new IllegalStateException("Failed to initialize window");
		}
		
		glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
		glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

		//Make the OpenGL context current
		glfwMakeContextCurrent(glfwWindow);
		
		//Enable v-sync
		glfwSwapInterval(1);
		
		//Make the window visible
		glfwShowWindow(glfwWindow);
				
		//Create Capabilities
		createCapabilities();
		
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		glfwSetInputMode(glfwWindow, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

		System.out.println("WINDOW CREATED!");
	}
	
	public static Window get() {
		if(Window.instance == null) {
			Window.instance = new Window();
		}
		return Window.instance;
	}

	public long getGLFWWindow() {
		return this.glfwWindow;
	}
}
