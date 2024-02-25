package com.components;

import java.util.Map;

import com.utils.Animation;

public class AnimationComponent extends Component {	
	public Map<String, Animation> animations;
	private Animation currentAnimation;
	private int frame = 0;
	private float elapsed = 0;
	private TextureComponent spritesheet;
	private float FPS;
	
	public AnimationComponent(Map<String, Animation> animations, String currentAnimation) {
		this.animations = animations;
		setCurrentAnimation(currentAnimation);
	}

	@Override
	public void start() {
		this.spritesheet = this.gameObject.getComponent(TextureComponent.class);
	}
	
	@Override
	public void update(float dt) {
		if(frame != -1) {
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
			
			spritesheet.setSpritePosition(currentAnimation.getSx() + frame, currentAnimation.getY());
			this.gameObject.setDirty(true);
		}
	}
	
	public Animation setCurrentAnimation(String i) {
		currentAnimation = animations.get(i);
		frame = 0;
		elapsed = 0;
		FPS = (float) currentAnimation.getTotalFrames() / currentAnimation.getAnimationDuration();
		return currentAnimation;
	}
	
	public int getCurrentFrame() {
		return frame;
	}
}
