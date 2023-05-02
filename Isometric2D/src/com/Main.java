package com;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import org.lwjgl.opengl.GL11;

import com.scenes.Scene;
import com.scenes.TestScene;

import listeners.KeyListener;
import util.Helper;

public class Main {
	
	private static Scene scene;
	private static Renderer renderer;
	
	public static void main(String[] args) {
		Window.get().init();
		Main main = new Main();
		scene = new TestScene();
		scene.start();
		renderer = new Renderer();
		main.loop();
	}
	
	public void loop() {
		float beginTime = (float)glfwGetTime();
		float endTime = (float)glfwGetTime();
		float dt = -1;
		
		while(!glfwWindowShouldClose(Window.get().getGLFWWindow())) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			
			scene.update(dt);
			renderer.render();
			
			glfwSwapBuffers(Window.get().getGLFWWindow());
			glfwPollEvents();

			if(KeyListener.isKeyPressed(GLFW_KEY_F)) {
				System.out.println("FPS: " + 1/dt);
			}
			
			if(KeyListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
				break;
			}
			
			endTime = (float)glfwGetTime();
			dt = endTime - beginTime;
			beginTime = endTime;
		}
		
		Helper.dispose();
	}
	
	public static Scene getScene(){
		return scene;
	}
	
	public static Renderer getRenderer() {
		return renderer;
	}

}
