#version 330 core

in vec2 fTexCoords;
in vec4 fTexData;
in vec3 fTexColor;

uniform sampler2D uTextures[8];
uniform float uAlpha;
uniform vec2 uDimensions[8];
uniform vec2 uSpriteDimensions[8];
uniform vec2 uSpritePosition;

void main(){
	int index = int(fTexData.z);
	vec2 cell = vec2(uSpriteDimensions[index] / uDimensions[index]);
	vec2 uv = (fTexCoords * cell) + (cell * fTexData.xy);
	vec4 tex = texture(uTextures[index], uv);
	
	bool isColorScale = fTexColor.x != -1;
	if(isColorScale){
		float greyScale = dot(tex.rgb, vec3(1, 1, 1));
		vec3 rgb = fTexColor * vec3(greyScale);
		gl_FragData[0] = vec4(rgb, tex.a);
	}else{
		gl_FragData[0] = tex;
	}
}
