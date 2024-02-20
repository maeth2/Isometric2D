package com.utils;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1iv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class ShaderLoader {	
	/**
	 * Find and build GLSL shader program from directory
	 * 
	 * @param path		Path to shader folder
	 * @return			Shader program ID
	 */
	public static int buildShader(String path) {
		int vertexID = 0, fragmentID = 0, shaderID;

		File file = new File(path);
		for(File f : file.listFiles()) {
			String p = f.getPath();
			String type = p.substring(p.length() - 4);
			if(type.equals("vert")) {
				vertexID = loadShader(p, GL_VERTEX_SHADER);
			}else if(type.equals("frag")) {
				fragmentID = loadShader(p, GL_FRAGMENT_SHADER);
			}
		}
		
		shaderID = glCreateProgram();
		glAttachShader(shaderID, vertexID);
		glAttachShader(shaderID, fragmentID);
		glLinkProgram(shaderID);
		
		if(glGetProgrami(shaderID, GL_LINK_STATUS) == GL_FALSE) {
			System.out.println("LINKING UNSUCCESSFUL!");
			int len = glGetProgrami(shaderID, GL_INFO_LOG_LENGTH);
			System.out.println(glGetProgramInfoLog(shaderID, len));
			glDeleteShader(shaderID);
			shaderID = -1;
		}else {
			System.out.println("LINKING SUCCESSFUL!");
		}
		
		return shaderID;
	}
	
	/**
	 * Find and load GLSL shader
	 * 
	 * @param file		File path of shader code
	 * @param type		OpenGL specified type of shader
	 * @return			Shader ID
	 */
	private static int loadShader(String file, int type){
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null){
				shaderSource.append(line).append("\n");
			}
			reader.close();
		}catch (IOException e){
			System.err.println("Could not read file");
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = glCreateShader(type); // Creates a shader object
		glShaderSource(shaderID, shaderSource); // Inputs shader code into shader object
		glCompileShader(shaderID); // Compiles shader code into shader object
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){ //Checks and prints any compilation errors
			System.out.println(glGetShaderInfoLog(shaderID,500));
			System.err.println("Could not compile shader");
			System.exit(-1);
		}
		return shaderID;
	}
	
	/**
	 * Binds the attribute number to shader variable
	 * 
	 * @param programID			Shader program ID
	 * @param attribute			Attribute number to bind to
	 * @param variableName		Variable name to bind to
	 */
	public static void bindAttribute(int programID, int attribute, String variableName){ 
		glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Uploads Matrix4f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Matrix4f to upload
	 */
	public static void loadMatrix(int shaderID, String name, Matrix4f data){
		int location = glGetUniformLocation(shaderID, name);
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		data.get(buffer);
		glUniformMatrix4fv(location, false, buffer);
	}
	
	/**
	 * Uploads Integer into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Integer to upload	 
	 */
	public static void loadInt(int shaderID, String name, int data) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1i(location, data);
	}
	
	/**
	 * Uploads Boolean into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Boolean to upload	 
	 */
	public static void loadBool(int shaderID, String name, boolean data) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1i(location, data ? 1 : 0);
	}
	
	/**
	 * Uploads Float into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Float to upload	 
	 */
	public static void loadFloat(int shaderID, String name, float data) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1f(location, data);
	}
	
	/**
	 * Uploads Vector3f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Vector3f to upload	 
	 */
	public static void loadVector3f(int shaderID, String name, Vector3f data){//Changes vector variables in shader program
		int location = glGetUniformLocation(shaderID, name);
		GL20.glUniform3f(location, data.x, data.y, data.z);
	}
	
	/**
	 * Uploads Array of Vector3f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Vector3f array to upload	 
	 */
	public static void loadVector3fArray(int shaderID, String name, Vector3f data[]){//Changes vector variables in shader program
		int location = glGetUniformLocation(shaderID, name);
		float array[] = new float[data.length * 3];
		for(int i = 0; i < data.length; i++) {
			array[i * 3] = data[i].x;
			array[i * 3 + 1] = data[i].y;
			array[i * 3 + 2] = data[i].z;
		}
		GL20.glUniform3fv(location, array);
	}
	
	/**
	 * Uploads Vector2f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Vector2f to upload	 
	 */
	public static void loadVector2f(int shaderID, String name, Vector2f data){//Changes vector variables in shader program
		int location = glGetUniformLocation(shaderID, name);
		GL20.glUniform2f(location, data.x, data.y);
	}
	
	/**
	 * Uploads Array of Vector2f into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Vector2f array to upload	 
	 */
	public static void loadVector2fArray(int shaderID, String name, Vector2f data[]){//Changes vector variables in shader program
		int location = glGetUniformLocation(shaderID, name);
		float array[] = new float[data.length * 2];
		for(int i = 0; i < data.length; i++) {
			array[i * 2] = data[i].x;
			array[i * 2 + 1] = data[i].y;
		}
		GL20.glUniform2fv(location, array);
	}
	
	/**
	 * Uploads Vector2i into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Vector2i to upload	 
	 */
	public static void loadVector2i(int shaderID, String name, Vector2i data){//Changes vector variables in shader program
		int location = glGetUniformLocation(shaderID, name);
		GL20.glUniform2i(location, data.x, data.y);
	} 
	
	/**
	 * Uploads Integer into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param data				Array to upload	 
	 */
	public static void loadIntArray(int shaderID, String name, int[] data) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1iv(location, data);
	}
	
	/**
	 * Uploads texture into uniform variable in shader
	 * 
	 * @param shaderID			Shader program ID
	 * @param name				Name of uniform variable
	 * @param texture			Texture to upload
	 */
	public static void loadTexture(int shaderID, String name, int texture) {
		int location = glGetUniformLocation(shaderID, name);
		glUniform1i(location, texture);
	}
	
	/**
	 * Binds and uses shader
	 * @param shaderID			Shader program ID
	 */
	public static void useShader(int shaderID) {
		glUseProgram(shaderID);
	}
	
	/**
	 * Detaches shader from program
	 */
	public static void unbindShader() {
		glUseProgram(0);
	}
}
