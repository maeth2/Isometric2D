package com.particles;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

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
	
	public void add(String text, Vector2f position, float size, float duration) {
		add(text, position, size, new Vector2f(0, 0), duration, new Vector3f(-1, -1, -1));
	}
	
	public void add(String text, Vector2f position, float size, float duration, Vector3f color) {
		add(text, position, size, new Vector2f(0, 0), duration, color);
	}
	
	public void add(String text, Vector2f position, float size, Vector2f velocity, float duration) {
		add(text, position, size, velocity, duration, new Vector3f(-1, -1, -1));
	}
	
	public void add(String text, Vector2f position, float size, Vector2f velocity, float duration, Vector3f color) {
		List<Particle> particles = TextParticle.createTextParticle(text, position, size, velocity, duration, color);
		for (Particle p : particles) {
			add(p);
		}
	}
	
	public void update(float dt) {
		for(int i = 0; i < batches.size(); i++) {
			ParticleBatch b = batches.get(i);
			b.update(dt);
			if(b.isEmpty()) {
				batches.remove(i);
				continue;
			}
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
