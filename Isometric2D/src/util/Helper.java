package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL30.*;

public class Helper {
	private static List<Integer> vaos = new ArrayList<Integer>();
	private static List<Integer> vbos = new ArrayList<Integer>();
	
	/**
	 * Creates an OpenGL VAO
	 * 
	 * @return 		VAO ID
	 */
	public static int generateVAO() {
		int id = glGenVertexArrays();
		vaos.add(id);
		return id;
	}
	
	/**
	 * Creates an OpenGL VBO
	 * 
	 * @return 		VBO ID
	 */
	private static int generateVBO() {
		int id = glGenBuffers();
		vbos.add(id);
		return id;
	}
	
	/**
	 * Creates a OpenGL VBO and stores indices as an element buffer
	 * 
	 * @param vaoID		VAO ID
	 * @param array		Integer array to be stored into VBO
	 * @return 			VBO ID
	 */
	public static int generateIndicesBuffer(int vaoID, int[] array) {
		glBindVertexArray(vaoID);
		int id = generateVBO();

		IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
		buffer.put(array).flip();
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		glBindVertexArray(0);
		return id;
	}
	
	/**
	 * Stores Data into attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param size			Size of each group of data
	 * @param attribute		Attribute number to store data
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int size, int attribute, float[] array) {
		glBindVertexArray(vaoID);
		
		int id = generateVBO();
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
		
		return id;
	}
	
	
	/**
	 * Cleans up all VAOs and VBOs from memory
	 */
	public static void dispose() {
		System.out.println("CLEANING UP!");
		for(int vao : vaos) {
			glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			glDeleteVertexArrays(vbo);
		}
	}
}
