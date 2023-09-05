package com.components.shaders;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import com.GameObject;
import com.Main;
import com.Renderer;
import com.components.LightComponent;
import com.components.TextureComponent;

import util.AssetManager;
import util.BufferHelper;
import util.Maths;
import util.Quad;
import util.ShaderLoader;
import util.Texture;

public class ShadowShaderComponent extends ShaderComponent {
	//TODO
	/**
	 * 1. Add Documentation
	 * 2. Fix Gaussian Blur issue create an example using alpha test program
	 */
		
	int occlusionShader;
	
	int shadow1DShader;
	
	int shadowShader;
	
	int rayNumber = 360 * 10;
		
	List<LightStructure> lights = new ArrayList<LightStructure>();
		
	class LightStructure{
		int occlusionFBO;
		Texture occlusionTexture;
		
		int shadow1DFBO;
		Texture shadow1DTexture;
		
		int shadowFBO;
		Texture shadowTexture;
		
		int diameter;
		GameObject lightObject;
		LightComponent lightComponent;
		
		Vector2f lastPos;
			
		public LightStructure(GameObject lightObject) {
			this.lightObject = lightObject;
			this.lightComponent = lightObject.getComponent(LightComponent.class);
			this.diameter = (int)lightComponent.getRadius() * 2;
			this.occlusionFBO = BufferHelper.createFrameBuffer(diameter, diameter, 2);
			this.shadow1DFBO = BufferHelper.createFrameBuffer(rayNumber, 1, 1);
			this.shadowFBO = BufferHelper.createFrameBuffer(diameter, diameter, 1);
			this.occlusionTexture = AssetManager.generateBufferTexture(occlusionFBO, diameter, diameter, 1, Texture.TYPE_ALPHA);
			this.shadow1DTexture = AssetManager.generateBufferTexture(shadow1DFBO, rayNumber, 1, 0, Texture.TYPE_ALPHA);
			this.shadowTexture = AssetManager.generateBufferTexture(shadowFBO, diameter, diameter, 0, Texture.TYPE_ALPHA);
			
			this.lastPos = new Vector2f();
		}
	}
	 
	public ShadowShaderComponent(int width, int height) {
		this.width = width;
		this.height = height;
		this.skip = true;
	}
	
	@Override
	public void start() {
		this.occlusionShader = AssetManager.getShader("assets/shaders/default");
		this.shadow1DShader = AssetManager.getShader("assets/shaders/shadow/shadow1D");
		this.shadowShader = AssetManager.getShader("assets/shaders/shadow/shadow");
		this.shader = AssetManager.getShader("assets/shaders/default");
	
		this.fbo = BufferHelper.createFrameBuffer(width, height, 1);
		
		this.texture = AssetManager.generateBufferTexture(fbo, width, height, 0, Texture.TYPE_ALPHA);
	}
	
	public void addLight(GameObject light) {
		LightStructure l = new LightStructure(light);
		lights.add(l);
		System.out.println("Number of Lights in scene: " + lights.size());
	}
	
	public void setup(int fbo, int shader, Texture texture) {
		BufferHelper.bindFrameBuffer(
			fbo, 
			(int)texture.getWidth(), 
			(int)texture.getHeight()
		);
		ShaderLoader.useShader(shader);
		Main.getScene().getCamera().createOrthographicProjection(texture.getWidth(), texture.getHeight());
		ShaderLoader.loadMatrix(shader, "uProjection", Main.getScene().getCamera().getProjectionMatrix());
	}
	
	public void unbind() {
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
	}
	
	public Texture renderShadow(LightStructure light, int i) {
		Main.getScene().getCamera().setTarget(light.lightObject);
		
		System.out.println("Rendering Shadow: " + i);
		
		//Rendering all shadow objects
		setup(light.occlusionFBO, occlusionShader, light.occlusionTexture);
		Renderer.refresh();
		ShaderLoader.loadMatrix(occlusionShader, "uView", Main.getScene().getCamera().getViewMatrix());	
		for(GameObject e : Main.getScene().getGameObjects()) {
			TextureComponent tex = e.getComponent(TextureComponent.class);
			if(tex != null) {
				ShaderLoader.loadBool(occlusionShader, "uShadow", tex.canCastShadow());
				Quad.renderQuad(occlusionShader, tex.getTexture(), Maths.createTransformationalMatrix(e.transform));
			}
		}
		unbind();
		
		//Converting Occlusion texture to 1D Shadow Map
		setup(light.shadow1DFBO, shadow1DShader, light.shadow1DTexture);
		Renderer.refresh();
		ShaderLoader.loadFloat(shadow1DShader, "uRadius", rayNumber);
		Quad.renderQuad(shadow1DShader, light.occlusionTexture, 0, 0,  light.shadow1DTexture.getWidth(),light.shadow1DTexture.getHeight());
		unbind();
				
		//Converting 1D Shadow Map to Shadow Mask
		setup(light.shadowFBO, shadowShader, light.shadowTexture);
		ShaderLoader.loadFloat(shadowShader, "uRadius", light.diameter);
		ShaderLoader.loadFloat(shadowShader, "uIntensity", light.lightComponent.getIntensity());
		ShaderLoader.loadVector3f(shadowShader, "uColor", light.lightComponent.getColor());
		Renderer.refresh();
		Quad.renderQuad(shadowShader, light.shadow1DTexture, 0, 0, light.shadowTexture.getWidth(), light.shadowTexture.getHeight());
		unbind();
		
		return light.shadowTexture;
	}
	
	public Texture renderLights() {
		setup(fbo, shader, texture);
		Renderer.refresh();
		unbind();
		GameObject old = Main.getScene().getCamera().getTarget();
		int i = 0;
		for(LightStructure light : lights) {
			if(!Main.checkInScreen(light.lightObject, light.diameter, light.diameter) || !light.lightComponent.canCastShadow()){
				continue;
			}
			boolean isDirty = light.lightObject.transform.position.x != light.lastPos.x || light.lightObject.transform.position.y != light.lastPos.y;
			Texture shadow = isDirty ? renderShadow(light, i) : light.shadowTexture;
			Main.getScene().getCamera().setTarget(old);
			
			//Scale down by half because the quad renderer vertices are from -1 to 1 not 0 to 1
			light.lightObject.transform.scale.x = light.diameter / 2;
			light.lightObject.transform.scale.y = light.diameter / 2;
			
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			setup(fbo, shader, texture);
			ShaderLoader.loadBool(shader, "uManualAlpha", false);
			ShaderLoader.loadMatrix(shader, "uView", Main.getScene().getCamera().getViewMatrix());
			Quad.renderQuad(shader, shadow, Maths.createTransformationalMatrix(light.lightObject.transform));
			unbind();
			
			light.lastPos.x = light.lightObject.transform.position.x;
			light.lastPos.y = light.lightObject.transform.position.y;
			i++;
		}
		Main.getScene().getCamera().setTarget(old);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		return texture;
	}
	
	@Override
	public Texture render(Texture[] t) {
		return null;
	}

}
