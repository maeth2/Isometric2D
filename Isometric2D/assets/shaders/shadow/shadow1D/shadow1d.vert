#version 330 core
layout(location=0) in vec2 aPos;

out vec2 fTexCoords;

uniform mat4 uTransformation;
uniform mat4 uProjection;
uniform mat4 uView;

void main(){
	fTexCoords = (vec2(aPos.x, aPos.y) + 1) / 2;
	gl_Position = uProjection * uTransformation * vec4(aPos, 0.0, 1.0);
}