#version 330 core

in vec2 fTexCoords;

uniform sampler2D uTexture;
uniform float uAlpha;
uniform bool uManualAlpha;
uniform bool uShadow;

out vec4 color;

void main(){
	if(uManualAlpha){
		gl_FragData[0] = vec4(texture(uTexture, fTexCoords).xyz, uAlpha);
	}else{
		gl_FragData[0] = texture(uTexture, fTexCoords);
	}
	gl_FragData[1] = vec4(uShadow);
}
