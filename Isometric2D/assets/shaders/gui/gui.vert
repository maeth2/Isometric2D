#version 330 core
layout(location=0) in vec2 aPos;

out vec2 fTexCoords;

uniform mat4 uTransformation;

float map(float value, float min1, float max1, float min2, float max2) {
 	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main(){
	fTexCoords =  vec2(map(aPos.x, -0.5, 0.5, 0, 1), map(aPos.y, -0.5, 0.5, 0, 1));
	gl_Position = uTransformation * vec4(aPos, 0.0, 1.0);
}