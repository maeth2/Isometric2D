#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTexture;

void main(){
	gl_FragData[0] = texture(uTexture, fTexCoords);
}