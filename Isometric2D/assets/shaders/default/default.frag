#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTexture;
uniform float uAlpha;
uniform bool uManualAlpha;
uniform bool uShadow;
uniform vec2 uSpriteDimension;
uniform vec2 uSpritePosition;
uniform vec2 uDimensions;

out vec4 color;

void main(){
	vec2 cell = vec2(uSpriteDimension / uDimensions);
	vec2 uv = (fTexCoords * cell) + (cell * uSpritePosition);
	if(uManualAlpha){
		gl_FragData[0] = vec4(texture(uTexture, uv).xyz, uAlpha);
	}else{
		gl_FragData[0] = texture(uTexture, uv);
	}
	gl_FragData[1] = vec4(uShadow);
}
