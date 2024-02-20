package com.utils;

public class AnimationFrames {
	private int sx, ex, y;  
	private float animationDuration;
	private float actionStartFrame, actionEndFrame;
	private int totalFrames;
	
	public AnimationFrames(int sx, int ex, int y, float animationDuration, float actionStartFrame, float actionEndFrame) {
		init(sx, ex, y, animationDuration, actionStartFrame, actionEndFrame);
	}
	
	public AnimationFrames(int sx, int ex, int y, float animationDuration) {
		init(sx, ex, y, animationDuration, 0, 0);
	}

	public void init(int sx, int ex, int y, float animationDuration, float actionStartFrame, float actionEndFrame) {
		this.sx = sx;
		this.ex = ex;
		this.y = y;
		this.animationDuration = animationDuration;
		this.actionStartFrame = actionStartFrame;
		this.actionEndFrame = actionEndFrame;
		this.totalFrames = ex - sx + 1;
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
}
