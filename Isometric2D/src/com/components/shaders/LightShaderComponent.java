package com.components.shaders;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.Camera;
import com.GameObject;
import com.Main;
import com.components.LightComponent;
import com.renderer.Renderer;
import com.utils.AssetManager;
import com.utils.BufferHelper;
import com.utils.Quad;
import com.utils.ShaderLoader;
import com.utils.Texture;
import com.utils.TextureLoader;

public class LightShaderComponent extends ShaderComponent {
	
	List<LightComponent> lights = new ArrayList<LightComponent>();
	ShadowShaderComponent shadow;
	Vector3f ambient = new Vector3f(0.6f, 0.6f, 0.6f);
	Texture textures[] = new Texture[2];
	
	public LightShaderComponent(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void start() {
		this.shader = AssetManager.getShader("assets/shaders/light");
		this.fbo = BufferHelper.createFrameBuffer(width, height, 1);
		this.texture = AssetManager.generateBufferTexture(fbo, width, height, 0, Texture.TYPE_COLOR);
		this.shadow = renderer.getComponent(ShadowShaderComponent.class);
	}
	
	public int loadLights() {
		int i = 0;
		for(LightComponent light : lights) {
			if(!Main.checkInScreen(light.gameObject, (int)light.getRadius() * 2, (int)light.getRadius() * 2)){
				continue;
			}
			loadLight("uLights[" + i + "]", light);
			i++;
		}
		return i;
	}
	
	public void loadLight(String name, LightComponent data) {
		ShaderLoader.loadVector2f(shader, name + ".position", data.gameObject.transform.position);
		ShaderLoader.loadVector3f(shader, name + ".color", data.getColor());
		ShaderLoader.loadFloat(shader, name + ".radius", data.getRadius());
		ShaderLoader.loadBool(shader, name + ".castShadow", data.canCastShadow());
	}
	
	public void addLight(GameObject light) {
		LightComponent component = light.getComponent(LightComponent.class);
		if(component != null) {
			this.lights.add(component);
			if(this.shadow != null) {
				this.shadow.addLight(light);
			}
		}
	}
	
	public void removeLight(GameObject light) {
		LightComponent component = light.getComponent(LightComponent.class);
		if(component != null) {
			this.lights.remove(component);
			if(this.shadow != null) {
				this.shadow.removeLight(light);
			}
		}
	}
	
	@Override
	public Texture render(Texture[] t) {
		Camera c = Main.getScene().getCamera();
		textures[0] = t[Texture.TYPE_COLOR];
		if(shadow != null) {
			textures[1] = shadow.renderLights();
		}
		BufferHelper.bindFrameBuffer(
			fbo, 
			(int)texture.getWidth(), 
			(int)texture.getHeight()
		);
		Renderer.refresh();
		ShaderLoader.useShader(shader);
		ShaderLoader.loadMatrix(shader, "uView", c.getViewMatrix());
		ShaderLoader.loadMatrix(shader, "uProjection", c.getProjectionMatrix());
		ShaderLoader.loadInt(shader, "uLightNum", loadLights());
		ShaderLoader.loadBool(shader, "uHasShadow", shadow != null);
		ShaderLoader.loadVector3f(shader, "uAmbient", ambient);
		TextureLoader.bindTextureToShader(shader, "uTexture", 0);
		TextureLoader.bindTextureToShader(shader, "uShadow", 1);
		
		Quad.renderQuad(shader, textures, c.transform.position.x, c.transform.position.y, width, height);
		ShaderLoader.unbindShader();
		BufferHelper.unbindFrameBuffer();
		return texture;
	}

}
