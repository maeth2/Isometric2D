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

	private static final int ATTRIBUTE_COUNT = 7;
	private static final int VERTEX_COUNT = 4;
	private static final int VERTEX_SIZE = 2;
	private static final int UV_SIZE = 4;
	private static final int COLOR_SIZE = 3;
	
	private static final int MAX_TEXTURES = 8;
	private List<TextureComponent> texturedObjects = new ArrayList<TextureComponent>();
	private	List<TextureComponent> dirtyObjects = new ArrayList<TextureComponent>();
	private List<Transform> texturedTransforms = new ArrayList<Transform>();
	private Map<Integer, Integer> textures = new HashMap<Integer, Integer>(); 
	private Vector2f dimensions[][] = new Vector2f[2][MAX_TEXTURES];
	private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8};

	private int batchSize = 0;
	private int vao;
	
	private int vbos[] = new int[ATTRIBUTE_COUNT];
	
	private float[] vertices;
	private float[] uvs;
	private float[] colors;
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
		this.vertices = new float[MAX_BATCH_SIZE * VERTEX_COUNT * VERTEX_SIZE];
		this.uvs = new float[MAX_BATCH_SIZE * VERTEX_COUNT * UV_SIZE];
		this.colors = new float[MAX_BATCH_SIZE * VERTEX_COUNT * COLOR_SIZE];
		this.translations = new float[4][MAX_BATCH_SIZE * VERTEX_COUNT * VERTEX_SIZE];
		Helper.generateIndicesBuffer(vao, loadIndices());
	}
	
	public boolean add(TextureComponent tex, Transform transform) {
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
		
		int index = Helper.binarySearch(
			texturedTransforms, 
			transform, 
			(a, b) -> {
				return getLayer(a) <= getLayer(b);
			}
		);
		
		texturedObjects.add(index, tex);
		texturedTransforms.add(index, transform);
		this.batchSize++;
		
		for(int i = index; i < batchSize; i++){
			updateVertices(texturedObjects.get(i), texturedTransforms.get(i), i);
		}
		
		updateAttributePointers();
		return true;
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
		
		int index = Helper.binarySearch(
			texturedTransforms, 
			tex.gameObject.transform, 
			(a, b) -> {
				return getLayer(a) <= getLayer(b);
			}
		);
	
		texturedObjects.add(index, tex);
		texturedTransforms.add(index, tex.gameObject.transform);
		this.batchSize++;
		
		for(int i = index; i < batchSize; i++){
			updateVertices(texturedObjects.get(i), texturedTransforms.get(i), i);
		}
		
		updateAttributePointers();
		return true;
	}
	
	public boolean remove(TextureComponent tex) {
		if(texturedObjects.contains(tex)) {
			int index = texturedObjects.indexOf(tex);
			texturedObjects.remove(index);
			texturedTransforms.remove(index);
			batchSize--;
			for(int i = index; i < batchSize; i++) {
				updateVertices(texturedObjects.get(i), texturedTransforms.get(i), i);
			}
			updateAttributePointers();
			return true;
		}
		return false;
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
	
	private boolean updateTransforms(TextureComponent tex) {
		int currIndex = texturedObjects.indexOf(tex);
		if(currIndex == -1) {
			return false;
		}
		
		Transform transform = texturedTransforms.get(currIndex);
		
		boolean left = (currIndex > 0) ? getLayer(texturedTransforms.get(currIndex - 1)) >= getLayer(transform) : true;
		boolean right = (currIndex < batchSize - 1) ? getLayer(texturedTransforms.get(currIndex + 1)) <= getLayer(transform) : true;
		if(left && right) {
			return false;
		}

		texturedObjects.remove(currIndex);
		texturedTransforms.remove(currIndex);
		
		int index = Helper.binarySearch(
			texturedTransforms, 
			transform, 
			(a, b) -> {
				return getLayer(a) <= getLayer(b);
			}
		);
		
		if(index == currIndex) {
			texturedObjects.add(currIndex, tex);
			texturedTransforms.add(currIndex, transform);
			return false;
		}

		texturedObjects.add(index, tex);
		texturedTransforms.add(index, transform);
		
		return true;
	}
	
	private float getLayer(Transform t) {
		return t.zLayer != -1 ? t.zLayer : t.position.y;
	}
		
	private void updateAttributePointers() {
		Helper.storeDataInAttributeList(vao, vbos[0], VERTEX_SIZE, 0, vertices);
		Helper.storeDataInAttributeList(vao, vbos[1], VERTEX_SIZE, 1, translations[0]);
		Helper.storeDataInAttributeList(vao, vbos[2], VERTEX_SIZE, 2, translations[1]);
		Helper.storeDataInAttributeList(vao, vbos[3], VERTEX_SIZE, 3, translations[2]);
		Helper.storeDataInAttributeList(vao, vbos[4], VERTEX_SIZE, 4, translations[3]);
		Helper.storeDataInAttributeList(vao, vbos[5], UV_SIZE, 5, uvs);
		Helper.storeDataInAttributeList(vao, vbos[6], COLOR_SIZE, 6, colors);
	}
	
	private void updateVertices(TextureComponent tex, Transform transform, int index) {
		for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i++) {
			vertices[i + index * VERTEX_COUNT * VERTEX_SIZE] = Quad.vertices[i];
		}
		
		for(int i = 0; i < VERTEX_COUNT * UV_SIZE; i+=UV_SIZE) {
			uvs[i + index * VERTEX_COUNT * UV_SIZE] = tex.getSpritePosition().x;
			uvs[i + index * VERTEX_COUNT * UV_SIZE + 1] =tex.getSpritePosition().y;
			uvs[i + index * VERTEX_COUNT * UV_SIZE + 2] = textures.get(tex.getTexture().getID());
			uvs[i + index * VERTEX_COUNT * UV_SIZE + 3] = tex.canCastShadow() ? 1 : 0;
		}
		
		for(int i = 0; i < VERTEX_COUNT * COLOR_SIZE; i+=COLOR_SIZE) {
			colors[i + index * VERTEX_COUNT * COLOR_SIZE] = tex.getSpriteColor().x;
			colors[i + index * VERTEX_COUNT * COLOR_SIZE + 1] = tex.getSpriteColor().y;
			colors[i + index * VERTEX_COUNT * COLOR_SIZE + 2] = tex.getSpriteColor().z;
		}
		
		for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i+=VERTEX_SIZE) {
			translations[0][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.position.x;
			translations[0][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.position.y;
			translations[1][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.scale.x;
			translations[1][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.scale.y;
			translations[2][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.rotation.x;
			translations[2][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.rotation.y;
			translations[3][i + index * VERTEX_COUNT * VERTEX_SIZE] = transform.pivot.x;
			translations[3][i + index * VERTEX_COUNT * VERTEX_SIZE + 1] = transform.pivot.y;
		}

	}
	
	private void dirtyFlag() {
		for(int i = 0; i < batchSize; i++) {
			TextureComponent tex = texturedObjects.get(i);
			if(tex.gameObject.isDirty()) {
				updateVertices(tex, texturedTransforms.get(i), i);
				dirtyObjects.add(tex);
			}
		}
		
		boolean flag = false;
		for(TextureComponent i : dirtyObjects) {
			flag = flag || updateTransforms(i);
		}
		
		if(flag) {
			for(int i = 0; i < batchSize; i++) {
				updateVertices(texturedObjects.get(i), texturedTransforms.get(i), i);
			}
		}
		
		updateAttributePointers();
		dirtyObjects.clear();
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
