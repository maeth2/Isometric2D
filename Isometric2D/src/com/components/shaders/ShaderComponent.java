package com.components.shaders;

import com.Renderer;
import com.utils.Texture;

public abstract class ShaderComponent {
	int fbo;
	int shader;
	Texture texture;
	public Renderer renderer;
	int width, height;
	boolean skip = false;
	
	/**
	 * Start function
	 */
	public abstract void start();
	
	/**
	 * Add required prerequisite shaders
	 */
	public void addRequirements() {}
	
	/**
	 * Render function
	 * @param texture			Array of availible input textures
	 * @return					Component texture
	 */
	public abstract Texture render(Texture[] texture);
	
	public boolean canSkip() {
		return this.skip;
	}
}
