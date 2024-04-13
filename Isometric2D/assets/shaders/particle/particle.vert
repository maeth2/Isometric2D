#version 330 core

layout(location=0) in vec2 aVert;
layout(location=1) in vec2 aTranslation;
layout(location=2) in vec2 aScale;
layout(location=3) in vec3 aColor;
layout(location=4) in vec2 aTexData;

out vec3 fColor;
out vec2 fTexCoords;
out vec2 fTexData;

uniform mat4 uProjection;
uniform mat4 uView;

vec4 getTransformedPosition(vec2 v, vec2 t, vec2 s){
	mat4 trans = mat4(
		1, 0, 0, t.x,
		0, 1, 0, t.y,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	mat4 scale = mat4(
		s.x, 0, 0, 0,
		0, s.y, 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	return vec4(v.x, v.y, 1, 1) * scale * trans;
}

float map(float value, float min1, float max1, float min2, float max2) {
 	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main(){
	fTexCoords = vec2(map(aVert.x, -0.5, 0.5, 0, 1), map(aVert.y, -0.5, 0.5, 0, 1));
	fTexData = aTexData;
	fColor = aColor;
	vec4 pos = getTransformedPosition(aVert, aTranslation, aScale);
	gl_Position = uProjection * uView * pos;
}