package com;

import com.components.TextureComponent;

import util.AssetManager;
import util.Maths;
import util.Quad;
import util.ShaderLoader;
import util.Texture;
import util.Transform;

public class Renderer {
	Transform t;
	Texture texture;
	int sceneShaderID;
	
	public Renderer() {
		this.sceneShaderID = AssetManager.getShader("assets/shaders/default");
	}

	public void render() {
		ShaderLoader.useShader(sceneShaderID);		
		ShaderLoader.loadMatrix(sceneShaderID, "uProjection", Main.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(sceneShaderID, "uView", Main.getScene().getCamera().getViewMatrix());		
		for(GameObject e : Main.getScene().getGameObjects()) {
			TextureComponent t = e.getComponent(TextureComponent.class);
			if(t != null) {
				Quad.renderQuad(t.getTexture(), Maths.createTransformationalMatrix(e.transform));
			}
		}
		ShaderLoader.unbindShader();
	}
}
