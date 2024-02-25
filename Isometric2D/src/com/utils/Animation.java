package com.utils;

public class Animation{
	private int sx, ex, y;  
	private float animationDuration;
	private float actionStartFrame, actionEndFrame;
	private int totalFrames;
	private boolean toggleOnce;
	
	public Animation(int sx, int ex, int y, float animationDuration, float actionStartFrame, float actionEndFrame, boolean toggleOnce) {
		this.sx = sx;
		this.ex = ex;
		this.y = y;
		this.animationDuration = animationDuration;
		this.actionStartFrame = actionStartFrame;
		this.actionEndFrame = actionEndFrame;
		this.totalFrames = ex - sx + 1;
		this.toggleOnce = toggleOnce;
	}
	
	public float getAnimationDuration() {
		return animationDuration;
	}

	public int getSx() {
		return sx;
	}

	public int getEx() {
		return ex;
	}

	public int getY() {
		return y;
	}

	public int getTotalFrames() {
		return totalFrames;
	}

	public float getActionStartFrame() {
		return actionStartFrame;
	}

	public float getActionEndFrame() {
		return actionEndFrame;
	}
	
	public boolean isToggleOnce() {
		return toggleOnce;
	}
	
	/**
	 * Create new Animation
	 * 
	 * @param sx						Starting x position in spritesheet
	 * @param ex						Ending x position in spritesheet
	 * @param y							Starting y position in spritesheet
	 * @param animationDuration			Animation duration in seconds
	 * @param actionStartFrame			Frame when action begins
	 * @param actionEndFrame			Frame when action ends
	 * @return							New animation object
	 */
	public static Animation createAnimationFrame(int sx, int ex, int y, float animationDuration, float actionStartFrame, float actionEndFrame) {
		return new Animation(sx, ex, y, animationDuration, actionStartFrame, actionEndFrame, false);
	}
	
	/**
	 * Create new Animation
	 * 
	 * @param sx						Starting x position in spritesheet
	 * @param ex						Ending x position in spritesheet
	 * @param y							Starting y position in spritesheet
	 * @param animationDuration			Animation duration in seconds
	 * @return							New animation object
	 */
	public static Animation createAnimationFrame(int sx, int ex, int y, float animationDuration) {
		return new Animation(sx, ex, y, animationDuration, 0, ex - sx, false);
	}
	
	/**
	 * Create new Animation
	 * 
	 * @param sx						Starting x position in spritesheet
	 * @param ex						Ending x position in spritesheet
	 * @param y							Starting y position in spritesheet
	 * @param animationDuration			Animation duration in seconds
	 * @param toggleOnce				Animation only toggles once
	 * @return							New animation object
	 */
	public static Animation createAnimationFrame(int sx, int ex, int y, float animationDuration, boolean toggleOnce) {
		return new Animation(sx, ex, y, animationDuration, 0, ex - sx, toggleOnce);
	}
}
