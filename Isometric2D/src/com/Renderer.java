package com;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.components.TextureComponent;
import com.components.shaders.ShaderComponent;

import util.AssetManager;
import util.BufferHelper;
import util.Maths;
import util.Quad;
import util.ShaderLoader;
import util.Texture;

public class Renderer {
	private List<ShaderComponent> components = new ArrayList<ShaderComponent>();
	public static final float WIDTH = Window.WIDTH;
	public static final float HEIGHT = Window.HEIGHT;
	public static final float ASPECT_RATIO = (float) Window.WIDTH / (float) Window.HEIGHT;
	private int sceneShaderID;
	private int sceneBuffer;
	private Texture[] textures;
	
	boolean toggle = false;
	
	public Renderer() {
		this.sceneShaderID = AssetManager.getShader("assets/shaders/default");
		this.sceneBuffer = BufferHelper.createFrameBuffer(Window.WIDTH, Window.HEIGHT, 1);
		
		this.textures = new Texture[3];
		for(int i = 0; i < textures.length; i++) {
			textures[i] = new Texture();
		}

		textures[Texture.TYPE_COLOR] = AssetManager.generateBufferTexture(sceneBuffer, Window.WIDTH, Window.HEIGHT, 0, Texture.TYPE_COLOR);
	}
	
	/**
	 * Clear Buffer Bits
	 */
	public static void refresh() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	}

	public void render() {
		BufferHelper.bindFrameBuffer(sceneBuffer, Window.WIDTH, Window.HEIGHT);
		refresh();
		ShaderLoader.useShader(sceneShaderID);		
		Main.getScene().getCamera().createOrthographicProjection(Renderer.WIDTH, Renderer.HEIGHT);
		ShaderLoader.loadMatrix(sceneShaderID, "uProjection", Main.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(sceneShaderID, "uView", Main.getScene().getCamera().getViewMatrix());	
		ShaderLoader.loadBool(sceneShaderID, "uManualAlpha", true);
		for(GameObject e : Main.getScene().getGameObjects()) {
			TextureComponent t = e.getComponent(TextureComponent.class);
			if(t != null) {
				ShaderLoader.loadFloat(sceneShaderID, "uAlpha", t.getAlpha());
				Quad.renderQuad(sceneShaderID, t.getTexture(), Maths.createTransformationalMatrix(e.transform));
			}
		}
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
		textures[Texture.TYPE_OUTPUT].copy(textures[Texture.TYPE_COLOR]);
		for(ShaderComponent c : components) {
			if(c.canSkip()) continue;
			textures[Texture.TYPE_OUTPUT].copy(c.render(textures));
		}
		refresh();
		
		toggle = toggle ? true : true;
		Quad.renderQuad(toggle ? textures[Texture.TYPE_OUTPUT] : textures[Texture.TYPE_COLOR]);
	}
	
	/**
	 * Get component attached to scene
	 * 
	 * @param componentClass			Attached Component to check
	 * @return							Instance of component
	 */
	public <T extends ShaderComponent> T getComponent(Class<T> componentClass) {
		for(ShaderComponent s : components) {
			if(componentClass.isAssignableFrom(s.getClass())) {
				return componentClass.cast(s);
			}
		}
		return null;
	}
	
	/**
	 * Remove component from scene
	 * 
	 * @param componentClass			Component to detatch
	 */
	public <T extends ShaderComponent> void removeComponent(Class<T> componentClass) {
		for(int i = 0; i < components.size(); i++) {
			ShaderComponent s = components.get(i);
			if(componentClass.isAssignableFrom(s.getClass())) {
				components.remove(i);
				return;
			}
		}
	}

	/**
	 * Add component to scene
	 * 
	 * @param c			Component to add
	 * @return			Scene instance
	 */
	public Renderer addComponent(ShaderComponent c) {
		for(ShaderComponent s : components) {
			if(s.getClass().isAssignableFrom(c.getClass())){
				return this;
			}
		}
		c.renderer = this;
		c.start();
		c.addRequirements();
		components.add(c);
		return this;
	}
	
	/**
	 * Add component to scene
	 * 
	 * @param c			Component to add
	 * @return			Scene instance
	 */
	public Renderer addComponent(ShaderComponent c, int index) {
		components.add(index, c);
		c.start();
		c.renderer = this;
		return this;
	}
}
