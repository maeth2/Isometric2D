package com.components;

import java.util.Map;

import com.utils.AnimationFrames;

public class AnimationComponent extends Component {	
	public Map<String, AnimationFrames> animations;
	private String currentAnimation;
	private int frame = 0;
	private int prevFrame = 0;
	private float elapsed = 0;
	private TextureComponent spritesheet;
	private float animationDuration = 0.5f;
	
	private int x = 0;
	private int y = 0;
	private int totalFrames = 0;
	
	public AnimationComponent(Map<String, AnimationFrames> animations, String currentAnimation) {
		this.animations = animations;
		this.currentAnimation = currentAnimation;
	}

	@Override
	public void start() {
		this.spritesheet = this.gameObject.getComponent(TextureComponent.class);
	}
	
	@Override
	public void update(float dt) {
		elapsed += dt;
		float FPS = (float) totalFrames / animationDuration;
		frame = (int) (FPS * elapsed);
		if(prevFrame != frame) {
			prevFrame = frame;
			gameObject.setDirty(true);
		}
		if(frame >= totalFrames) {
			frame = 0;
			elapsed = 0;
		}
		spritesheet.setSpritePosition(x + frame, y);
	}
	
	public void setCurrentAnimation(String i) {
		this.currentAnimation = i;
		AnimationFrames frames = animations.get(currentAnimation);
		y = frames.getY();
		x = frames.getSx();
		totalFrames = frames.getTotalFrames();
		frame = 0;
		elapsed = 0;
	}
	
	public void setAnimationDuration(float i) {
		this.animationDuration = i;
	}
	
	public int getCurrentFrame() {
		return frame;
	}
}
