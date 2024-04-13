package com.particles;

import java.util.ArrayList;
import java.util.List;

import com.Main;
import com.utils.AssetManager;
import com.utils.ShaderLoader;

public class ParticleManager {

	List<ParticleBatch> batches = new ArrayList<ParticleBatch>();
	
	private int shaderID;
	
	public ParticleManager() {
		shaderID = AssetManager.getShader("assets/shaders/particle");
	}
	
	public void add(Particle p) {
		for(ParticleBatch b : batches) {
			if(b.getTextureID() == p.getTexture().getTexture().getID() && b.add(p)) {
				return;
			}
		}
		ParticleBatch batch = new ParticleBatch(p.getTexture());
		batches.add(batch);
		batch.add(p);
	}
	
	public void update(float dt) {
		for(ParticleBatch b : batches) {
			b.update(dt);
		}
	}
	
	public void render() {
		ShaderLoader.useShader(shaderID);
		ShaderLoader.loadMatrix(shaderID, "uProjection", Main.getScene().getCamera().getProjectionMatrix());
		ShaderLoader.loadMatrix(shaderID, "uView", Main.getScene().getCamera().getViewMatrix());	
		
		for(ParticleBatch b : batches) {
			b.render(shaderID);
		}
		
		ShaderLoader.unbindShader();
	}
}
