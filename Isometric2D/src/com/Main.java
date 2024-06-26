package com;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.util.Random;

import com.listeners.KeyListener;
import com.scenes.Scene;
import com.scenes.TestScene;
import com.utils.AssetManager;
import com.utils.BufferHelper;
import com.utils.Helper;

public class Main {
	
	private static Scene scene;
	public static float timeElapsed = 0f;
	public static Random random;
	
	public static void main(String[] args) {
		Window.get().init();
		Main main = new Main();
		random = new Random();
		scene = new TestScene(10, 10);
		scene.init();
		scene.start();
		main.loop();
	}
	
	public void loop() {
		float beginTime = (float)glfwGetTime();
		float endTime = (float)glfwGetTime();
		float dt = 0;
		
		while(!glfwWindowShouldClose(Window.get().getGLFWWindow())) {			
			scene.update(dt);
			timeElapsed += dt;
			
			glfwSwapBuffers(Window.get().getGLFWWindow());
			glfwPollEvents();

			if(KeyListener.isKeyPressed(GLFW_KEY_TAB)) {
				System.out.println("FPS: " + 1/dt);
				System.out.println("Time Elapsed: " + timeElapsed);
			}
			
			if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
				break;
			}
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		
		BufferHelper.dispose();
		Helper.dispose();
		AssetManager.dispose();
		System.gc();
	}
	
	public static boolean checkInScreen(GameObject g, int width, int height) {
		boolean isIn = false;
		float x = g.getTransform().getPosition().x;
		float y = g.getTransform().getPosition().y;
		float nx = x - getScene().getCamera().getTransform().getPosition().x;
		float ny = y - getScene().getCamera().getTransform().getPosition().y;
		
		float top = ny + height / 2;
		float bottom = ny - height / 2;
		float left = nx - width / 2;
		float right = nx + width / 2;
		
		isIn = right >= -Window.WIDTH / 2 && left <= Window.WIDTH / 2 && top >= -Window.HEIGHT / 2 && bottom <= Window.HEIGHT / 2;
		return isIn;
	}
	
	public static Scene getScene(){
		return scene;
	}
	
	public static float getTimeElapsed() {
		return timeElapsed;
	}
}
