#version 330 core

in vec3 fColor;
in vec2 fTexCoords;
in vec2 fTexData;

uniform sampler2D spriteSheet;
uniform vec2 dimensions;
uniform vec2 spriteDimensions;
uniform bool hasTexture;

void main(){
	if(hasTexture){
		vec2 cell = vec2(dimensions / spriteDimensions);
		vec2 uv = (fTexCoords * cell) + (fTexData * cell);
		gl_FragData[0] = texture(spriteSheet, uv);
	}else{
		gl_FragData[0] = vec4(fColor, 1.0);
	}
}