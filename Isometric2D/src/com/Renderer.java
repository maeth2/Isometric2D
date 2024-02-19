package com;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.components.TextureComponent;
import com.components.shaders.ShaderComponent;

import util.AssetManager;
import util.BufferHelper;
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
	private Texture[] bufferTextures;
	private List<RenderBatch> renderBatches = new ArrayList<RenderBatch>();
		
	public Renderer() {
		this.sceneShaderID = AssetManager.getShader("assets/shaders/batch");
		this.sceneBuffer = BufferHelper.createFrameBuffer(Window.WIDTH, Window.HEIGHT, 1);
		
		this.bufferTextures = new Texture[3];
		for(int i = 0; i < bufferTextures.length; i++) {
			bufferTextures[i] = new Texture();
		}

		bufferTextures[Texture.TYPE_COLOR] = AssetManager.generateBufferTexture(sceneBuffer, Window.WIDTH, Window.HEIGHT, 0, Texture.TYPE_COLOR);
	}
	
	/**
	 * Clear Buffer Bits
	 */
	public static void refresh() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Render all batches
	 * 
	 * @param shaderID			Shader to use
	 */
	public void renderBatches(int shaderID) {
		for(RenderBatch r : this.renderBatches) {
			r.render(shaderID);
		}
	}
	/**
	 * Render All Game Objects in Scene to Screen
	 * 
	 * @return							Instance of component
	 */
	public void render() {
		BufferHelper.bindFrameBuffer(sceneBuffer, Window.WIDTH, Window.HEIGHT);
		refresh();
		ShaderLoader.useShader(sceneShaderID);		
		ShaderLoader.loadMatrix(sceneShaderID, "uProjection", Main.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(sceneShaderID, "uView", Main.getScene().getCamera().getViewMatrix());	
		ShaderLoader.loadBool(sceneShaderID, "uManualAlpha", false);
		
		renderBatches(sceneShaderID);
		
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		
		bufferTextures[Texture.TYPE_OUTPUT].copy(bufferTextures[Texture.TYPE_COLOR]);
		for(ShaderComponent c : components) {
			if(c.canSkip()) continue;
			bufferTextures[Texture.TYPE_OUTPUT].copy(c.render(bufferTextures));
		}
		refresh();
		
		Quad.renderQuad(bufferTextures[Texture.TYPE_OUTPUT]);
	}
	

	
	/**
	 * Add GameObejct to render batch
	 * 
	 * @param o			GameObject to add
	 */
	public void addGameObject(GameObject o) {
		if(o.getComponent(TextureComponent.class) == null) return;
		TextureComponent tex = o.getComponent(TextureComponent.class);
		for(RenderBatch r : renderBatches) {
			if(r.add(tex)) {
				return;
			}
		}
		RenderBatch r = new RenderBatch();
		renderBatches.add(r);
		r.add(tex);
	}
	
	/**
	 * Get component attached to scene
	 * 
	 * @param componentClass		Attached Component to check
	 * @return						Instance of component
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
		components.add(c);
		c.addRequirements();
		return this;
	}
	
	/**
	 * Add component to scene
	 * 
	 * @param c			Component to add
	 * @param index		Index to add component
	 * @return			Scene instance
	 */
	public Renderer addComponent(ShaderComponent c, int index) {
		components.add(index, c);
		c.start();
		c.renderer = this;
		return this;
	}
}
