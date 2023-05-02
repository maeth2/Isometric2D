package util;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.stb.STBImage.*;

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
	 * Generate blank texture
	 * 
	 * @param frameBuffer		Frame Buffer ID
	 * @param width				Width of textures
	 * @param height			Height of texture
	 * @param attachement		OpenGL color attachement
	 * @param type				Type of texture
	 * @return					Texture ID
	 */
	public static Texture generateTexture(int frameBuffer, int width, int height, int attachment, int type) {
		int texture = glGenTextures();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		return new Texture(type, texture, width, height);
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
		glActiveTexture(GL_TEXTURE0 + slot);
		glBindTexture(GL_TEXTURE_2D, texture.getID());
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
