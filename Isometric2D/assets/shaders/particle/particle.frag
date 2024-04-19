#version 330 core

in vec3 fColor;
in vec2 fTexCoords;
in vec2 fTexData;

uniform sampler2D spriteSheet;
uniform vec2 spriteDimensions;
uniform vec2 dimensions;
uniform bool hasTexture;

void main(){
	if(hasTexture){
		vec2 cell = vec2(spriteDimensions / dimensions);
		vec2 uv = (fTexCoords * cell) + (fTexData * cell);
		vec4 tex = texture(spriteSheet, uv);
		if(fColor.x != -1){
			float greyScale = dot(tex.rgb, vec3(1, 1, 1));
			vec3 rgb = fColor * vec3(greyScale);
			gl_FragData[0] = vec4(rgb, tex.a);
		}else{
			gl_FragData[0] = tex;
		}
	}else{
		gl_FragData[0] = vec4(fColor, 1.0);
	}
}