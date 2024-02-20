package com;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2f;

import com.components.TextureComponent;
import com.utils.Helper;
import com.utils.Quad;
import com.utils.ShaderLoader;
import com.utils.TextureLoader;
import com.utils.Transform;

public class RenderBatch {
	private static final int MAX_BATCH_SIZE = 1000;

	private static final int ATTRIBUTE_COUNT = 5;
	private static final int VERTEX_COUNT = 4;
	private static final int VERTEX_SIZE = 2;
	private static final int SPRITE_SIZE = 4;
	
	private static final int MAX_TEXTURES = 8;
	private List<TextureComponent> texturedObjects = new ArrayList<TextureComponent>();
	private Map<Integer, Integer> textures = new HashMap<Integer, Integer>(); 
	private Vector2f dimensions[][] = new Vector2f[2][MAX_TEXTURES];
	private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8};

	private int batchSize = 0;
	private int vao;
	
	private int vbos[] = new int[ATTRIBUTE_COUNT];
	
	private float[] vertices;
	private float[] uvs;
	private float[][] translations;

	
	public RenderBatch() {
		this.vao = Helper.generateVAO();
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			vbos[i] = Helper.generateVBO();
		}
		for(int i = 0; i < MAX_TEXTURES; i++) {
			dimensions[0][i] = new Vector2f();
			dimensions[1][i] = new Vector2f();
		}
		this.vertices = new float[1000 * VERTEX_COUNT * VERTEX_SIZE];
		this.uvs = new float[1000 * VERTEX_COUNT * SPRITE_SIZE];
		this.translations = new float[3][1000 * VERTEX_COUNT * VERTEX_SIZE];
		Helper.generateIndicesBuffer(vao, loadIndices());
	}
	
	private int[] loadIndices() {
		int[] indices = new int[MAX_BATCH_SIZE * 6];
		
		for(int i = 0; i < MAX_BATCH_SIZE; i++) {
			for(int j = 0; j < 6; j++) {
				indices[i * 6 + j] = Quad.indices[j] + i * 4;
			}
		}
		
		return indices;
	}
	
	public boolean add(TextureComponent tex) {
		if(texturedObjects.size() == MAX_BATCH_SIZE) return false;
				
		if(texturedObjects.contains(tex)) {
			return true;
		}
		
		int texID = tex.getTexture().getID();
		
		if(!textures.containsKey(texID)) {
			if(textures.size() > MAX_TEXTURES) return false;
			int texIndex = textures.size();
			textures.put(texID, texIndex);
			dimensions[0][texIndex] = tex.getTexture().getDimensions();
			dimensions[1][texIndex] = tex.getSpriteDimension();
		}
		
		texturedObjects.add(tex);
		updateVertices(tex, this.batchSize);
		this.batchSize++;
		return true;
	}
		
	private void updateVertices(TextureComponent tex, int index) {
		Transform transform = tex.gameObject.transform;

		for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i++) {
			vertices[i + index * VERTEX_COUNT * VERTEX_SIZE] = Quad.vertices[i];
		}
		
		for(int i = 0; i < VERTEX_COUNT * SPRITE_SIZE; i+=SPRITE_SIZE) {
			uvs[i + index * VERTEX_COUNT * SPRITE_SIZE] = tex.getSpritePosition().x;
			uvs[i + index * VERTEX_COUNT * SPRITE_SIZE + 1] =tex.getSpritePosition().y;
			uvs[i + index * VERTEX_COUNT * SPRITE_SIZE + 2] = textures.get(tex.getTexture().getID());
			uvs[i + index * VERTEX_COUNT * SPRITE_SIZE + 3] = tex.canCastShadow() ? 1 : 0;
		}
		
		for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i+=VERTEX_SIZE) {
			translations[0][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.position.x;
			translations[0][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.position.y;
			translations[1][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.scale.x;
			translations[1][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.scale.y;
			translations[2][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.rotation.x;
			translations[2][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.rotation.y;
		}

		Helper.storeDataInAttributeList(vao, vbos[0], VERTEX_SIZE, 0, vertices);
		Helper.storeDataInAttributeList(vao, vbos[1], VERTEX_SIZE, 1, translations[0]);
		Helper.storeDataInAttributeList(vao, vbos[2], VERTEX_SIZE, 2, translations[1]);
		Helper.storeDataInAttributeList(vao, vbos[3], VERTEX_SIZE, 3, translations[2]);
		Helper.storeDataInAttributeList(vao, vbos[4], SPRITE_SIZE, 4, uvs);
	}
	
	private void dirtyFlag() {
		for(int i = 0; i < this.texturedObjects.size(); i++) {
			TextureComponent tex = texturedObjects.get(i);
			if(tex.gameObject.isDirty()) {
				updateVertices(tex, i);
				tex.gameObject.setDirty(false);
			}
		}
	}
	
	public void render(int shader) {
		dirtyFlag();
		
		glBindVertexArray(vao);
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			glEnableVertexAttribArray(i); 
		}
		
		for(int i : textures.keySet()) {
			TextureLoader.loadTextureToShader(shader, i, textures.get(i));
		}
		ShaderLoader.loadIntArray(shader, "uTextures", texSlots);
		
		ShaderLoader.loadVector2fArray(shader, "uDimensions", dimensions[0]);
		ShaderLoader.loadVector2fArray(shader, "uSpriteDimensions", dimensions[1]);
		
		glDrawElements(GL_TRIANGLES, this.batchSize * 6, GL_UNSIGNED_INT, 0);

		for(int i = 0; i < textures.size(); i++) {
			TextureLoader.unbindTexture(shader, i);
		}
		
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			glDisableVertexAttribArray(i);
		}
		
		glBindVertexArray(0);
	}
}
