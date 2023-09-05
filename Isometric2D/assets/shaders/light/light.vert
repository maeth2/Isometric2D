#version 330 core
layout(location=0) in vec2 aPos;

out vec2 fTexCoords;
out vec2 fPosition;

uniform mat4 uTransformation;
uniform mat4 uProjection;
uniform mat4 uView;

void main(){
	fTexCoords =  vec2((aPos.x+1.0)/2.0, (aPos.y+1.0)/2.0);
	vec4 worldPosition = uTransformation * vec4(aPos, 0.0, 1.0);
	gl_Position = uProjection * uView * worldPosition;
	fPosition = vec2(worldPosition.xy);
}