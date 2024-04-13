package com.renderer;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import com.GameObject;
import com.Main;
import com.Window;
import com.components.AABBComponent;
import com.components.TextureComponent;
import com.components.shaders.ShaderComponent;
import com.utils.AssetManager;
import com.utils.BufferHelper;
import com.utils.Quad;
import com.utils.ShaderLoader;
import com.utils.Texture;

public class Renderer {
	private List<ShaderComponent> components = new ArrayList<ShaderComponent>();
	public static final float WIDTH = Window.WIDTH;
	public static final float HEIGHT = Window.HEIGHT;
	public static final float ASPECT_RATIO = (float) Window.WIDTH / (float) Window.HEIGHT;
	private int sceneShaderID;
	private int sceneBuffer;
	private Texture[] bufferTextures;
	private List<List<RenderBatch>> renderBatches = new ArrayList<List<RenderBatch>>();
	private List<RenderBatch> shadowBatches = new ArrayList<RenderBatch>();
	private List<RenderBatch> debugBatches = new ArrayList<RenderBatch>();
	
	private boolean toggleDebug = false;
		
	public Renderer() {
		this.sceneShaderID = AssetManager.getShader("assets/shaders/batch");
		this.sceneBuffer = BufferHelper.createFrameBuffer(Window.WIDTH, Window.HEIGHT, 1);
		
		this.bufferTextures = new Texture[3];
		for(int i = 0; i < bufferTextures.length; i++) {
			bufferTextures[i] = new Texture();
		}
		
		for(int i = 0; i < 3; i++) {
			renderBatches.add(new ArrayList<RenderBatch>());
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
	 * Render object batches
	 * 
	 * @param shaderID			Shader to use
	 */
	public void renderBatches(int shaderID) {
		for(List<RenderBatch> r1 : this.renderBatches) {
			for(RenderBatch r : r1) {
				r.render(shaderID);
			}
		}
		if(toggleDebug) {
			for(RenderBatch r : this.debugBatches) {
				r.render(shaderID);
			}		
		}
	}
	
	/**
	 * Render shadow batches
	 * 
	 * @param shaderID			Shader to use
	 */
	public void renderShadowBatches(int shaderID) {
		for(RenderBatch r : this.shadowBatches) {
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
		
		Main.getScene().getParticles().render();
		
		BufferHelper.unbindFrameBuffer();
		
		bufferTextures[Texture.TYPE_OUTPUT].copy(bufferTextures[Texture.TYPE_COLOR]);
		for(ShaderComponent c : components) {
			if(c.canSkip()) continue;
			bufferTextures[Texture.TYPE_OUTPUT].copy(c.render(bufferTextures));
		}
		refresh();
		
		Quad.renderGUI(bufferTextures[Texture.TYPE_OUTPUT], new Vector2f(2, 2));
	}
	
	/**
	 * Add GameObject to render batch
	 * 
	 * @param o			GameObject to add
	 */
	public void addGameObject(GameObject o) {
		if(o.getComponent(TextureComponent.class) == null) return;
		TextureComponent tex = o.getComponent(TextureComponent.class);
		boolean added = false;
		for(RenderBatch r : renderBatches.get(o.getLayer())) {
			if(r.add(tex)) {
				added = true;
				break;
			}
		}
		
		if(!added) {
			RenderBatch r = new RenderBatch();
			renderBatches.get(o.getLayer()).add(r);
			r.add(tex);
		}
		
		if(tex.canCastShadow()) {
			AABBComponent aabb = tex.gameObject.getComponent(AABBComponent.class);
			if(aabb != null) {
				added = false;
				aabb.getTexture().gameObject = o;
				aabb.getTexture().setCastShadow(tex.canCastShadow());
				for(RenderBatch r : shadowBatches) {
					if(r.add(aabb.getTexture(), aabb.getTransform())) {
						added = true;
						break;
					}
				}
				if(!added) {
					RenderBatch r = new RenderBatch();
					shadowBatches.add(r);
					r.add(aabb.getTexture());
				}
			}
		}
		
		AABBComponent aabb = tex.gameObject.getComponent(AABBComponent.class);
		if(aabb != null) {
			added = false;
			aabb.getTexture().gameObject = o;
			for(RenderBatch r : debugBatches) {
				if(r.add(aabb.getTexture(), aabb.getTransform())) {
					added = true;
					break;
				}
			}
			if(!added) {
				RenderBatch r = new RenderBatch();
				debugBatches.add(r);
				r.add(aabb.getTexture());
			}
		}
	}
	
	/**
	 * Remove GameObject from renderBatch
	 * 
	 * @param o			GameObject to remove
	 */
	public void removeGameObject(GameObject o) {
		if(o.getComponent(TextureComponent.class) == null) return;
		TextureComponent tex = o.getComponent(TextureComponent.class);
		for(RenderBatch r : renderBatches.get(o.getLayer())) {
			if(r.remove(tex)) {
				break;
			}
		}
		
		AABBComponent aabb = tex.gameObject.getComponent(AABBComponent.class);
		if(aabb != null) {
			for(RenderBatch r : shadowBatches) {
				if(r.remove(aabb.getTexture())) {
					break;
				}
			}
			for(RenderBatch r : debugBatches) {
				if(r.remove(aabb.getTexture())) {
					break;
				}
			}
		}
	}
	
	/**
	 * Upadate GameObject layer
	 * @param o						GameObject to update
	 * @param layer					Layer to update to
	 */
	public void updateGameObjectLayer(GameObject o, int layer) {
		removeGameObject(o);
		o.updateLayer(layer);
		addGameObject(o);
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
	
	/**
	 * Toggle debug mode
	 */
	public void toggleDebug() {
		toggleDebug = !toggleDebug;
	}
}
