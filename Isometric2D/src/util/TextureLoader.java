package util;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


public class TextureLoader {

	/**
	 * Load texture from file directory
	 * 
	 * @param filePath		Image directory
	 * @return				OpenGL texture
	 */
	public static Texture loadTexture(String filePath) {
		int id = glGenTextures();
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = stbi_load(filePath, width, height, channels, 0);
		
		
		glBindTexture(GL_TEXTURE_2D, id);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		if(image != null) {
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
		}else {
			glDeleteTextures(id);
			id = -1;
			System.out.println("Unable to load image: " + filePath);
		}
		
		stbi_image_free(image);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		return new Texture(id, width.get(0), height.get(0));
	}
	
	/**
	 * Generate blank buffer texture
	 * 
	 * @param fbo		Frame Buffer ID
	 * @param width				Width of textures
	 * @param height			Height of texture
	 * @param attachement		OpenGL color attachement
	 * @param type				Type of texture
	 * @return					Texture ID
	 */
	public static Texture generateBufferTexture(int fbo, int width, int height, int attachment, int type) {
		int textureID = glGenTextures();
		Texture texture = new Texture(type, textureID, width, height);
		BufferHelper.loadBufferTexture(fbo, texture, attachment);
		return texture;
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param uniformName			Sampler2D uniform variable name
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void loadTextureToShader(int shaderID, String uniformName, int textureFile, int slot) {
		ShaderLoader.loadTexture(shaderID, uniformName, slot);
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, textureFile);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void loadTextureToShader(int shaderID, Texture texture, int slot) {
		loadTextureToShader(shaderID, texture.getID(), slot);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void loadTextureToShader(int shaderID, int textureID, int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void bindTextureToShader(int shaderID, String name, int slot) {
		ShaderLoader.loadInt(shaderID, name, slot);
	}
	
	/**
	 * Loads a texture to shader
	 * 
	 * @param shaderID				Shader ID
	 * @param textureFile			Image ID
	 * @param slot					OpenGL texture slot to use
	 */
	public static void unbindTexture(int shaderID, int slot) {
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
