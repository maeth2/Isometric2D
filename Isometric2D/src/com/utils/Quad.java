package com.utils;

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.Window;

public class Quad {
	
	public static float vertices[] = {
			-0.5f, 0.5f,
			-0.5f, -0.5f,
			0.5f, 0.5f,
			0.5f, -0.5f
	};
	
	public static int indices[] = {
			0, 1, 2,
			1, 2, 3
	};
	
	private static int vaoID;
	
	private static int shaderID;
	
	private static Matrix4f base = Maths.createTransformationalMatrix(new Transform());
	
	/**
	 * Render Quad
	 * 
	 * @param texture		Quad Texture
	 */
	public static void renderGUI(Texture texture, Vector2f scale) {
		ShaderLoader.useShader(getShaderID());
		renderQuad(getShaderID(), texture, Maths.createTransformationalMatrix(new Transform(new Vector2f(), scale)));
		ShaderLoader.unbindShader();
	}
	
	/**
	 * Render Quad
	 * 
	 * @param shader		Shader ID to Use
	 * @param texture		Quad Texture
	 */
	public static void renderGUI(int shader, Texture texture) {
		renderQuad(shader, texture, null);
	}
	
	/**
	 * Render Quad
	 * 
	 * @param texture		Quad Texture
	 * @param x				X Position
	 * @param y				Y Position
	 * @param width			Quad Width
	 * @param height		Quad Height
	 */
	public static void renderQuad(Texture texture, float x, float y, float width, float height) {
		ShaderLoader.useShader(getShaderID());
		renderQuad(getShaderID(), texture, x, y, width, height, null);
		ShaderLoader.unbindShader();
	}
	
	/**
	 * Render Quad
	 * 
	 * @param shader		Shader ID to Use
	 * @param texture		Quad Texture
	 * @param x				X Position
	 * @param y				Y Position
	 * @param width			Quad Width
	 * @param height		Quad Height
	 */
	public static void renderQuad(int shader, Texture texture, float x, float y, float width, float height) {
		renderQuad(shader, texture, x, y, width, height, null);
	}
	
	/**
	 * Render Quad
	 * 
	 * @param texture			Quad Texture
	 * @param transformation	Transformation Matrix
	 */
	public static void renderQuad(Texture texture, Matrix4f transformation) {
		ShaderLoader.useShader(getShaderID());
		renderQuad(getShaderID(), texture, transformation);
		ShaderLoader.unbindShader();
	}

	/**
	 * Render Quad
	 * 
	 * @param shader			Shader ID to Use
	 * @param texture			Quad Texture
	 * @param x					X Position
	 * @param y					Y Position
	 * @param width				Quad Width
	 * @param height			Quad Height
	 * @param transformation	Transformation Matrix
	 */
	public static void renderQuad(int shader, Texture texture, float x, float y, float width, float height, Matrix4f transformation) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 

		if(transformation != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", transformation);
		}else {
			ShaderLoader.loadMatrix(
					shader, 
					"uTransformation", 
					Maths.createTransformationalMatrix(new Transform(
							new Vector2f(x, y),
							new Vector2f(width, height)
					))
			);
		}
		
		if(texture != null) {
			TextureLoader.loadTextureToShader(shader, texture, 0);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			TextureLoader.unbindTexture(shader, 0);
		}
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	/**
	 * Render Quad
	 * 
	 * @param shader			Shader ID to Use
	 * @param texture			Quad Texture
	 * @param transformation	Transformation Matrix
	 */
	public static void renderQuad(int shader, Texture texture, Matrix4f transformation) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 

		if(transformation != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", transformation);
		}else {
			ShaderLoader.loadMatrix(shader, "uTransformation", base);
		}
				
		TextureLoader.loadTextureToShader(shader, texture, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		TextureLoader.unbindTexture(shader, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	
	/**
	 * Render Quad
	 * 
	 * @param shader		Shader ID to Use
	 * @param texture		Quad Texture
	 */
	public static void renderQuad(int shader, Texture[] texture) {
		renderQuad(shader, texture, 0, 0, Window.WIDTH, Window.HEIGHT, null);
	}
	
	/**
	 * Render Quad
	 * 
	 * @param shader			Shader ID to Use
	 * @param textures			Textures to Load into Shader
	 * @param x					X Position
	 * @param y					Y Position
	 * @param width				Quad Width
	 * @param height			Quad Height
	 */
	public static void renderQuad(int shader, Texture[] textures, float x, float y, float width, float height) {
		renderQuad(shader, textures, x, y, width, height, null);
	}
	
	/**
	 * Render Quad
	 * 
	 * @param shader			Shader ID to Use
	 * @param textures			Textures to Load into Shader
	 * @param x					X Position
	 * @param y					Y Position
	 * @param width				Quad Width
	 * @param height			Quad Height
	 * @param transformation	Transformation Matrix
	 */
	public static void renderQuad(int shader, Texture[] textures, float x, float y, float width, float height, Matrix4f transformation) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 
		
		if(transformation != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", transformation);
		}else {
			ShaderLoader.loadMatrix(
					shader, 
					"uTransformation", 
					Maths.createTransformationalMatrix(new Transform(
							new Vector2f(x, y),
							new Vector2f(width, height)
					))
			);
		}

		for(int i = 0; i < textures.length; i++) {
			if(textures[i] == null) continue;
			TextureLoader.loadTextureToShader(shader, textures[i], i);
		}
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		for(int i = 0; i < textures.length; i++) {
			TextureLoader.unbindTexture(shader, i);
		}
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	public static void renderQuadTest(int shader, Texture texture, Transform transform) {
		glBindVertexArray(getVaoID());
		glEnableVertexAttribArray(0); 

		if(transform != null) {
			ShaderLoader.loadMatrix(shader, "uTransformation", Maths.createTransformationalMatrix(transform));
		}else {
			ShaderLoader.loadMatrix(shader, "uTransformation", base);
		}
		
		ShaderLoader.loadVector2f(shader, "uTranslation", transform.getPosition());
		ShaderLoader.loadVector2f(shader, "uRotation", transform.getRotation());
		ShaderLoader.loadVector2f(shader, "uScale", transform.getScale());
				
		TextureLoader.loadTextureToShader(shader, texture, 0);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		TextureLoader.unbindTexture(shader, 0);
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	public static int getVaoID() {
		if(vaoID == 0) {
			vaoID = Helper.generateVAO();
			Helper.storeDataInAttributeList(getVaoID(), 2, 0, vertices);
		}
		return vaoID;
	}
	
	public static int getShaderID() {
		if(shaderID == 0) {
			shaderID = AssetManager.getShader("assets/shaders/gui");
		}
		return shaderID;
	}
}
