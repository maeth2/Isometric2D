package com.particles;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import com.components.TextureComponent;
import com.utils.Helper;
import com.utils.Quad;
import com.utils.ShaderLoader;
import com.utils.TextureLoader;

public class ParticleBatch {
	private static final int MAX_PARTICLES = 1000;
	
	private static final int VERTEX_COUNT = 4;
	private static final int ATTRIBUTE_COUNT = 5;

	private static final int VERTEX_SIZE = 2;
	private static final int POSITION_SIZE = 2;
	private static final int SCALE_SIZE = 2;
	private static final int COLOR_SIZE = 3;
	private static final int TEX_SIZE = 2;
	
	private int vao;
	private int vbos[] = new int[ATTRIBUTE_COUNT];
	
	private TextureComponent texture;
	private boolean hasTexture;
	
	List<Particle> particles = new ArrayList<Particle>();
	
	float vertices[];
	float positions[];
	float scales[];
	float colors[];
	float tex[];
	
	public ParticleBatch(TextureComponent texture) {
		init(texture);
	}
	
	public void init(TextureComponent texture) {
		this.texture = texture;
		hasTexture = texture.getColor().x == -1;
		
		vao = Helper.generateVAO();
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			vbos[i] = Helper.generateVBO();
		}
		vertices = new float[MAX_PARTICLES * VERTEX_COUNT * VERTEX_SIZE];
		positions = new float[MAX_PARTICLES * VERTEX_COUNT * POSITION_SIZE];
		scales = new float[MAX_PARTICLES * VERTEX_COUNT * SCALE_SIZE];
		colors = new float[MAX_PARTICLES * VERTEX_COUNT * COLOR_SIZE];
		tex = new float[MAX_PARTICLES * VERTEX_COUNT * TEX_SIZE];
		for(int index = 0; index < MAX_PARTICLES; index++) {
			for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i++) {
				vertices[i + index * VERTEX_COUNT * VERTEX_SIZE] = Quad.vertices[i];
			}
		}
		Helper.generateIndicesBuffer(vao, loadIndices());
	}
	
	private int[] loadIndices() {
		int[] indices = new int[MAX_PARTICLES * 6];
		for(int i = 0; i < MAX_PARTICLES; i++) {
			for(int j = 0; j < 6; j++) {
				indices[i * 6 + j] = Quad.indices[j] + i * 4;
			}
		}
		return indices;
	}
	
	public boolean add(Particle p) {
		if(particles.size() > MAX_PARTICLES) {
			return false;
		}
		
		particles.add(p);
		
		return true;
	}
	
	public void update(float dt) {
		for(int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			p.update(dt);
			if(p.checkDuration()) {
				particles.remove(i);
			}
		}
		updateVertices();
		updateAttributePointers();
	}
	
	private void updateVertices() {
		for(int index = 0; index < particles.size(); index++) {
			Particle p = particles.get(index);
			for(int i = 0; i < VERTEX_COUNT; i++) {
				positions[i * POSITION_SIZE + index * VERTEX_COUNT * POSITION_SIZE] = p.getPosition().x;
				positions[i * POSITION_SIZE + index * VERTEX_COUNT * POSITION_SIZE + 1] = p.getPosition().y;
				scales[i * SCALE_SIZE + index * VERTEX_COUNT * SCALE_SIZE] = p.getScale().x;
				scales[i * SCALE_SIZE + index * VERTEX_COUNT * SCALE_SIZE + 1] = p.getScale().y;
				colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE] = p.getTexture().getColor().x;
				colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE + 1] = p.getTexture().getColor().y;
				colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE + 2] = p.getTexture().getColor().z;
				tex[i * TEX_SIZE + index * VERTEX_COUNT * TEX_SIZE] = p.getTexture().getSpritePosition().x;
				tex[i * TEX_SIZE + index * VERTEX_COUNT * TEX_SIZE + 1] = p.getTexture().getSpritePosition().y;
			}
		}
	}
	
	private void updateAttributePointers() {
		Helper.storeDataInAttributeList(vao, vbos[0], VERTEX_SIZE, 0, vertices);
		Helper.storeDataInAttributeList(vao, vbos[1], POSITION_SIZE, 1, positions);
		Helper.storeDataInAttributeList(vao, vbos[2], SCALE_SIZE, 2, scales); 
		Helper.storeDataInAttributeList(vao, vbos[3], COLOR_SIZE, 3, colors); 
		Helper.storeDataInAttributeList(vao, vbos[4], TEX_SIZE, 4, tex); 
	}
	
	public int getTextureID() {
		return this.texture.getTexture().getID();
	}
	
	public void render(int shader) {		
		glBindVertexArray(vao);
		if(hasTexture) {
			TextureLoader.bindTextureToShader(shader, "spriteSheet", 0);
			TextureLoader.loadTextureToShader(shader, texture.getTexture().getID(), 0);
			ShaderLoader.loadVector2f(shader, "dimensions", texture.getTexture().getDimensions());
			ShaderLoader.loadVector2f(shader, "spriteDimensions", texture.getSpriteDimensions());
			ShaderLoader.loadBool(shader, "hasTexture", true);
		}else {
			ShaderLoader.loadBool(shader, "hasTexture", false);
		}
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			glEnableVertexAttribArray(i); 
		}
		
		glDrawElements(GL_TRIANGLES, particles.size() * 6, GL_UNSIGNED_INT, 0);
		
		TextureLoader.unbindTexture(shader, 0);

		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			glDisableVertexAttribArray(i);
		}
		
		glBindVertexArray(0);
	}
}
