package util;

import org.joml.Matrix4f;

public class Maths {
	/**
	 * Creates transformational matrix from a transform
	 * 
	 * @param transform			Base transforms
	 * @return					Transformation matrix
	 */
	public static Matrix4f createTransformationalMatrix(Transform transform){
		Matrix4f matrix = new Matrix4f();
		matrix.identity().translate(transform.position.x, transform.position.y, 0).
        	rotateZ((float)Math.toRadians(transform.rotation.x)).
        	rotateY((float)Math.toRadians(transform.rotation.y)).
        	scale(transform.scale.x, transform.scale.y, 1);
		return matrix;
	}
	
	/**
	 * Creates a view matrix from a transform
	 * 
	 * @param transform			Base transform
	 * @return					View matrix
	 */
	public static Matrix4f createViewMatrix(Transform transform) {
		Matrix4f matrix = new Matrix4f();
		matrix = matrix.rotationZ((float)Math.toRadians(transform.rotation.x))
		         .rotateY((float)Math.toRadians(transform.rotation.y))
		         .translate(-transform.position.x, -transform.position.y, 0);
		return matrix;
	}
	
	/**
	 * Creates a orthographic projection matrix from screen width and height
	 * 
	 * @param width				Screen width
	 * @param height			Screen height
	 * @return					Projection matrix
	 */	
	public static Matrix4f createOrthographicProjection(float width, float height) {
		float w = width / 2;
		float h = height / 2;
	    Matrix4f ortho = new Matrix4f().ortho2D(-w, w, -h, h);
	    return ortho;
	}
}
