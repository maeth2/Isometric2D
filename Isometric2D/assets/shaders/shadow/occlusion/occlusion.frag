#version 330 core

in vec4 fTexData;

void main(){
	gl_FragData[0] = vec4(fTexData.w);
}
