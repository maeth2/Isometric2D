package util;

import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL20.glDeleteShader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetManager {
	public static Map<String, Texture> textures = new HashMap<>();
	public static Map<String, Integer> shaders = new HashMap<>();
	
	/**
	 * Loads texture from system
	 * 
	 * @param filePath				Image file path
	 * @return						Texture ID
	 */
	public static Texture getTexture(String filePath) {
		String file = new File(filePath).getAbsolutePath();
		if(textures.containsKey(file)) {
			return textures.get(file);
		}else {
			Texture t = TextureLoader.loadTexture(file);
			if(t.getID() > -1) {
				textures.put(file, t);
			}
			return t;
		}
	}
	
	/**
	 * Generates blank buffer texture
	 * 
	 * @param frameBuffer			Frame Buffer ID
	 * @param width					Width of texture
	 * @param height				Height of texture
	 * @param attachement			OpenGL color attachment
	 * @param type 					Type of texture
	 * 
	 * @return						Texture ID
	 */
	public static Texture generateBufferTexture(int frameBuffer, int width, int height, int attachment, int type) {
		Texture texture = TextureLoader.generateBufferTexture(frameBuffer, width, height, attachment, type);
		textures.put(Integer.toString(type), texture);
		return texture;
	}

	/**
	 * Loads shader from assets
	 * 
	 * @param filePath				Shader file path
	 * @return						Shader ID
	 */
	public static int getShader(String filePath) {
		String file = new File(filePath).getAbsolutePath();
		if(shaders.containsKey(file)) {
			return shaders.get(file);
		}else {
			int shaderID = ShaderLoader.buildShader(file);
			if(shaderID > -1) {
				shaders.put(file, shaderID);
			}
			return shaderID;
		}
	}

	/**
	 * Cleans up all textures and shaders from memory
	 */
	public static void dispose() {
		for(String s : shaders.keySet()) {
			glDeleteShader(shaders.get(s));
		}
		for(String t : textures.keySet()) {
			glDeleteTextures(textures.get(t).getID());
		}
	}
}
