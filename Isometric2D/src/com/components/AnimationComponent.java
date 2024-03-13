package com.components;

import java.util.Map;

import com.utils.Animation;

public class AnimationComponent extends Component {	
	public Map<Enum<?>, Animation> animations;
	private TextureComponent spritesheet;
	private Animation currentAnimation;
	private boolean freeze = false;
	private int frame = 0;
	private int totalFrames = 0;
	private float elapsed = 0;
	private float FPS;
	private boolean isReversed = false;
	
	public AnimationComponent(Map<Enum<?>, Animation> animations, Enum<?> currentAnimation) {
		this.animations = animations;
		setCurrentAnimation(currentAnimation);
	}

	@Override
	public void start() {
		this.spritesheet = this.gameObject.getComponent(TextureComponent.class);
	}
	
	@Override
	public void update(float dt) {
		if(!freeze && frame != -1) {
			elapsed += dt;
			frame = (int) (FPS * elapsed);
	
			if(frame >= currentAnimation.getTotalFrames()) {
				if(currentAnimation.isToggleOnce()) {
					frame = -1;
					return;
				}else {
					frame = 0;
					elapsed = 0;
				}
			}								
			
			if(!isReversed) {
				spritesheet.setSpritePosition(currentAnimation.getSx() + frame, currentAnimation.getY());
			}else {
				spritesheet.setSpritePosition(currentAnimation.getSx() + totalFrames - frame - 1, currentAnimation.getY());
			}
			
			this.gameObject.setDirty(true);
		}
	}
	
	public Animation setCurrentAnimation(Enum<?> i) {
		currentAnimation = animations.get(i);
		frame = 0;
		totalFrames = currentAnimation.getTotalFrames();
		elapsed = 0;
		FPS = (float) currentAnimation.getTotalFrames() / currentAnimation.getAnimationDuration();
		return currentAnimation;
	}
	
	public Animation getCurrentAnimation(){
		return this.currentAnimation;
	}
	
	public int getCurrentFrame() {
		return frame;
	}
	
	public void setFreeze(boolean i) {
		this.freeze = i;
	}
	
	public void setReversed(boolean i) {
		if(i != isReversed) {
			isReversed = i;
			frame = 0;
			elapsed = 0;
		}
	}
}
