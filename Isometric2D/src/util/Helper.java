package util;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import org.lwjgl.BufferUtils;

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
	public static int generateVBO() {
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
	 * Stores Data into existing attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param vboID			VBO ID
	 * @param size			Size of each group of data
	 * @param attribute		Attribute number to store data
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int vboID, int size, int attribute, float[] array) {
		glBindVertexArray(vaoID);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindVertexArray(0);
		
		return vboID;
	}
	
	/**
	 * Stores Data into attribute list
	 * 
	 * @param vaoID			VAO ID
	 * @param size			Size of each set of data
	 * @param groups		Attribute number and size of each sub-group in each group of data; 
	 * 						i[0] = attribute to store
	 * 						i[1] = size of sub-group
	 * @param array			Float array to be stored into VBO
	 * @return 				VBO ID
	 */
	public static int storeDataInAttributeList(int vaoID, int size, int[][] groups, float[] array) {
		glBindVertexArray(vaoID);
		int id = generateVBO();
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
		buffer.put(array).flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
		int skip = 0;
		for(int[] i : groups) {
			setAttributePointers(vaoID, i[0], i[1], size - i[1], skip);
			skip += i[1];
		}
		
		glBindVertexArray(0);
		
		return id;
	}
	
	/**
	 * Sets pointer for attribute list
	 * 
	 * @param vaoID				VAO ID
	 * @param attribute			Attribute number to store VBO
	 * @param size				Number of data put into each index of VBO
	 * @param skip				Number of data to skip in array
	 * @param offset			Number of data to skip at the start of reading
	 */
	public static void setAttributePointers(int vaoID, int attribute, int size, int skip, int offset) {
		glVertexAttribPointer(attribute, size, GL_FLOAT, false, (size + skip) * Float.BYTES, offset * Float.BYTES);
		glEnableVertexAttribArray(attribute);
	}
	
	
	/**
	 * Reverse a given array
	 * 
	 * @param a 			Array to invert
	 * 
	 * @return				Reversed array
	 */
	public static <T> T[] reverse(T[] a){
		@SuppressWarnings("unchecked")
		T[] inverted = (T[]) Array.newInstance(a[0].getClass(), a.length);
		for(int i = 0; i < a.length; i++) {
			inverted[i] = a[a.length - i - 1];
		}
		return inverted;
	}
	
	/**
	 * Sort a given List
	 * 
	 * @param a				List to sort
	 * @param compare		Comparator function
	 * @return				Sorted list
	 */
	public static <T> List<T> sort(List<T> a, BiFunction<T, T, Boolean> compare){
		List<T> sorted = new ArrayList<T>();
		for(T i : a) {
			int l = 0;
			int r = sorted.size() - 1;
			while(l <= r) {
				int m = (l + r) / 2;
				if(compare.apply(i, sorted.get(m))) {
					l = m + 1;
				}else {
					r = m - 1;
				}
			}
			if(sorted.size() == 0) {
				sorted.add(i);
			}else {
				sorted.add(l, i);
			}
		}
		return sorted;
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
