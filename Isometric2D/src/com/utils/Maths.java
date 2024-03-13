package com.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;

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
	
	/**
	 * Converts angle in degrees to a normalised 2D directional vector
	 * 
	 * @param angle				Angle in degrees
	 * @return					Normalised 2D directional vector
	 */
	public static Vector2f angleToDirectionVector(float angle) {
		float dx = (float) Math.cos(Math.toRadians(angle));
		float dy = (float) Math.sin(Math.toRadians(angle));
		return new Vector2f(dx, dy);
	}
	
	/**
	 * Get normalised direction vector from two points
	 * 
	 * @param p1				Point 1
	 * @param p2				Point 2
	 * @return					Normalised direction vector
	 */
	public static Vector2f pointToPointDirectionVector(Vector2f p1, Vector2f p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;
		return normalise(dx, dy);
	}
	
	
	/**
	 * Normalises Vector2f
	 * 
	 * @param dx				Vector x component
	 * @param dy				Vector y component
	 * @return					Normalised Vector
	 */
	public static Vector2f normalise(float dx, float dy) {
		float l = (float)Math.sqrt(dx * dx + dy * dy);
		return new Vector2f(dx / l, dy / l);
	}
}
