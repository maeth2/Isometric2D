#version 330 core

in vec2 fTexCoords;
in vec4 fTexData;

uniform sampler2D uTextures[8];
uniform float uAlpha;
uniform bool uManualAlpha;
uniform vec2 uDimensions[8];
uniform vec2 uSpriteDimensions[8];
uniform vec2 uSpritePosition;

out vec4 color;

void main(){
	int index = int(fTexData.z);
	vec2 cell = vec2(uSpriteDimensions[index] / uDimensions[index]);
	vec2 uv = (fTexCoords * cell) + (cell * fTexData.xy);
	if(uManualAlpha){
		gl_FragData[0] = vec4(texture(uTextures[index], uv).xyz, uAlpha);
	}else{
		gl_FragData[0] = texture(uTextures[index], uv);
	}
}
