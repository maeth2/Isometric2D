package com.components;

import util.Texture;

public class TextureComponent extends Component {
	private Texture texture;
	public TextureComponent(Texture texture) {
		this.texture = texture;
	}
	
	@Override
	public void update(float dt) {
	}
	
	public Texture getTexture() {
		return texture;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
}
