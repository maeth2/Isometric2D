package com.components.shaders;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import com.Camera;
import com.GameObject;
import com.Main;
import com.Window;
import com.components.LightComponent;
import com.renderer.Renderer;
import com.utils.AssetManager;
import com.utils.BufferHelper;
import com.utils.Maths;
import com.utils.Quad;
import com.utils.ShaderLoader;
import com.utils.Texture;
import com.utils.Transform;

public class ShadowShaderComponent extends ShaderComponent {
	//TODO
	/**
	 * 1. Add Documentation
	 */
		
	int occlusionShader;
	
	int shadow1DShader;
	
	int shadowShader;
	
	int rayNumber = 360 * 10;
		
	Camera tempCam = new Camera(new Vector2f(0, 0));
		
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
			this.occlusionTexture = AssetManager.generateBufferTexture(occlusionFBO, diameter, diameter, 0, Texture.TYPE_ALPHA);
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
		this.occlusionShader = AssetManager.getShader("assets/shaders/shadow/occlusion");
		this.shadow1DShader = AssetManager.getShader("assets/shaders/shadow/shadow1D");
		this.shadowShader = AssetManager.getShader("assets/shaders/shadow/shadow");
		this.shader = AssetManager.getShader("assets/shaders/shadow/default");
	
		this.fbo = BufferHelper.createFrameBuffer(width, height, 1);
		
		this.texture = AssetManager.generateBufferTexture(fbo, width, height, 0, Texture.TYPE_ALPHA);
	}
	
	@Override
	public void addRequirements() {
		if(this.renderer.getComponent(LightShaderComponent.class) == null) {
			this.renderer.addComponent(new LightShaderComponent(Window.WIDTH, Window.HEIGHT));
		}
	}
	
	public void addLight(GameObject light) {
		LightStructure l = new LightStructure(light);
		lights.add(l);
//		System.out.println("Number of Lights in scene: " + lights.size());
	}
	
	public void removeLight(GameObject light) {
		for(int i = 0; i < lights.size(); i++) {
			if(lights.get(i).lightObject == light) {
				lights.remove(i);
				break;
			}
		}
		System.out.println("Number of Lights in scene: " + lights.size());
	}
	
	public void setup(int fbo, int shader, Texture texture) {
		BufferHelper.bindFrameBuffer(
			fbo, 
			(int)texture.getWidth(), 
			(int)texture.getHeight()
		);
		ShaderLoader.useShader(shader);
		ShaderLoader.loadMatrix(shader, "uProjection", Maths.createOrthographicProjection(texture.getWidth(), texture.getHeight()));
	}
	
	public void unbind() {
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
	}
	
	public Texture renderShadow(LightStructure light, int i) {
		tempCam.setPosition(light.lightObject.getTransform().getPosition());
		tempCam.setGridPosition(Main.getScene().getGridPosition(tempCam));
		
		//Rendering all shadow objects
		setup(light.occlusionFBO, occlusionShader, light.occlusionTexture);
		Renderer.refresh();
		ShaderLoader.loadMatrix(occlusionShader, "uView", tempCam.getViewMatrix());	

		renderer.renderShadowBatches(occlusionShader);
		
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
		
		int i = 0;
		for(LightStructure light : lights) {
			if(!Main.checkInScreen(light.lightObject, light.diameter, light.diameter) || !light.lightComponent.canCastShadow()){
				continue;
			}
			Transform lightTransform = new Transform(light.lightObject.getTransform().getPosition(), new Vector2f(light.diameter, light.diameter));

			boolean isDirty = lightTransform.getPosition().x != light.lastPos.x || lightTransform.getPosition().y != light.lastPos.y;
			Texture shadow = isDirty ? renderShadow(light, i) : light.shadowTexture; //Check if light has moved.
			
			//Scale down by half because the quad renderer vertices are from -1 to 1 not 0 to 1
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			setup(fbo, shader, texture);
			ShaderLoader.loadMatrix(shader, "uView", Main.getScene().getCamera().getViewMatrix());
			Quad.renderQuad(shader, shadow, Maths.createTransformationalMatrix(lightTransform));
			unbind();
			
			light.lastPos.x = lightTransform.getPosition().x;
			light.lastPos.y = lightTransform.getPosition().y;
			i++;
		}
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		return texture;
	}
	
	@Override
	public Texture render(Texture[] t) {
		return null;
	}

}
