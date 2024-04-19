package com.renderer;

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
	private static final int POSITION_SIZE = 2;
	private static final int SCALE_SIZE = 2;
	private static final int ROTATION_SIZE = 2;
	private static final int PIVOT_SIZE = 2;
	private static final int UV_SIZE = 4;
	private static final int COLOR_SIZE = 3;
	
	private List<TextureComponent> texturedObjects = new ArrayList<TextureComponent>();
	private	List<TextureComponent> dirtyObjects = new ArrayList<TextureComponent>();
	private List<Transform> texturedTransforms = new ArrayList<Transform>();
	
	private int vao;
	private int vbos[] = new int[ATTRIBUTE_COUNT];
	
	private static final int MAX_TEXTURES = 8;
	private Map<Integer, Integer> textures = new HashMap<Integer, Integer>(); 
	private Vector2f dimensions[][] = new Vector2f[2][MAX_TEXTURES];
	private int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7, 8};
	
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
		this.translations = new float[4][0];
		this.translations[0] = new float[MAX_BATCH_SIZE * VERTEX_COUNT * POSITION_SIZE];
		this.translations[1] = new float[MAX_BATCH_SIZE * VERTEX_COUNT * SCALE_SIZE];
		this.translations[2] = new float[MAX_BATCH_SIZE * VERTEX_COUNT * ROTATION_SIZE];
		this.translations[3] = new float[MAX_BATCH_SIZE * VERTEX_COUNT * PIVOT_SIZE];
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
			dimensions[1][texIndex] = tex.getSpriteDimensions();
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
		
		for(int i = index; i < texturedObjects.size(); i++){
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
			dimensions[1][texIndex] = tex.getSpriteDimensions();
		}
		
		int index = Helper.binarySearch(
			texturedTransforms, 
			tex.gameObject.getTransform(), 
			(a, b) -> {
				return getLayer(a) <= getLayer(b);
			}
		);
	
		texturedObjects.add(index, tex);
		texturedTransforms.add(index, tex.gameObject.getTransform());
		
		for(int i = index; i < texturedObjects.size(); i++){
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
			for(int i = index; i < texturedObjects.size(); i++) {
				updateVertices(texturedObjects.get(i), texturedTransforms.get(i), i);
			}
			updateAttributePointers();
			return true;
		}
		return false;
	}
	
	private boolean updateTransforms(TextureComponent tex) {
		int currIndex = texturedObjects.indexOf(tex);
		if(currIndex == -1) {
			return false;
		}
		
		Transform transform = texturedTransforms.get(currIndex);
		
		boolean left = (currIndex > 0) ? getLayer(texturedTransforms.get(currIndex - 1)) >= getLayer(transform) : true;
		boolean right = (currIndex < texturedObjects.size() - 1) ? getLayer(texturedTransforms.get(currIndex + 1)) <= getLayer(transform) : true;
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
		return t.getZLayer() != -1 ? t.getZLayer() : t.getPosition().y;
	}
		
	private void updateAttributePointers() {
		Helper.storeDataInAttributeList(vao, vbos[0], VERTEX_SIZE, 0, vertices);
		Helper.storeDataInAttributeList(vao, vbos[1], POSITION_SIZE, 1, translations[0]);
		Helper.storeDataInAttributeList(vao, vbos[2], SCALE_SIZE, 2, translations[1]);
		Helper.storeDataInAttributeList(vao, vbos[3], ROTATION_SIZE, 3, translations[2]);
		Helper.storeDataInAttributeList(vao, vbos[4], PIVOT_SIZE, 4, translations[3]);
		Helper.storeDataInAttributeList(vao, vbos[5], UV_SIZE, 5, uvs);
		Helper.storeDataInAttributeList(vao, vbos[6], COLOR_SIZE, 6, colors);
	}
	
	private void updateVertices(TextureComponent tex, Transform transform, int index) {
		for(int i = 0; i < VERTEX_COUNT * VERTEX_SIZE; i++) {
			vertices[i + index * VERTEX_COUNT * VERTEX_SIZE] = Quad.vertices[i];
		}
		
		for(int i = 0; i < VERTEX_COUNT; i++) {
			uvs[i * UV_SIZE + index * VERTEX_COUNT * UV_SIZE] = tex.getSpritePosition().x;
			uvs[i * UV_SIZE + index * VERTEX_COUNT * UV_SIZE + 1] = tex.getSpritePosition().y;
			uvs[i * UV_SIZE + index * VERTEX_COUNT * UV_SIZE + 2] = textures.get(tex.getTexture().getID());
			uvs[i * UV_SIZE + index * VERTEX_COUNT * UV_SIZE + 3] = tex.canCastShadow() ? 1 : 0;
			colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE] = tex.getColor().x;
			colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE + 1] = tex.getColor().y;
			colors[i * COLOR_SIZE + index * VERTEX_COUNT * COLOR_SIZE + 2] = tex.getColor().z;
			translations[0][i * POSITION_SIZE + index * VERTEX_COUNT * POSITION_SIZE] = transform.getPosition().x;
			translations[0][i * POSITION_SIZE + index * VERTEX_COUNT * POSITION_SIZE + 1] = transform.getPosition().y;
			translations[1][i * SCALE_SIZE + index * VERTEX_COUNT * SCALE_SIZE] = transform.getScale().x;
			translations[1][i * SCALE_SIZE + index * VERTEX_COUNT * SCALE_SIZE + 1] = transform.getScale().y;
			translations[2][i * ROTATION_SIZE + index * VERTEX_COUNT * ROTATION_SIZE] = transform.getRotation().x;
			translations[2][i * ROTATION_SIZE + index * VERTEX_COUNT * ROTATION_SIZE + 1] = transform.getRotation().y;
			translations[3][i * PIVOT_SIZE + index * VERTEX_COUNT * PIVOT_SIZE] = transform.getPivot().x;
			translations[3][i * PIVOT_SIZE + index * VERTEX_COUNT * PIVOT_SIZE + 1] = transform.getPivot().y;
		}
	}
	
	private void dirtyFlag() {
		for(int i = 0; i < texturedObjects.size(); i++) {
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
			for(int i = 0; i < texturedObjects.size(); i++) {
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
		
		glDrawElements(GL_TRIANGLES, texturedObjects.size() * 6, GL_UNSIGNED_INT, 0);

		for(int i = 0; i < textures.size(); i++) {
			TextureLoader.unbindTexture(shader, i);
		}
		
		for(int i = 0; i < ATTRIBUTE_COUNT; i++) {
			glDisableVertexAttribArray(i);
		}
		
		glBindVertexArray(0);
	}
	
	public boolean isEmpty() {
		return texturedObjects.isEmpty();
	}
}
