#version 330 core

layout(location=0) in vec2 aVert;
layout(location=1) in vec2 aTranslation;
layout(location=2) in vec2 aScale;
layout(location=3) in vec2 aRotation;
layout(location=4) in vec2 aPivot;
layout(location=5) in vec4 aTexData;

out vec2 fTexCoords;
out vec4 fTexData;

uniform mat4 uProjection;
uniform mat4 uView;


vec4 getTransformedPosition(vec2 v, vec2 t, vec2 s, vec2 r, vec2 p){
	float sz = sin(radians(r.x + 180));
	float cz = cos(radians(r.x + 180));
	float sy = sin(radians(r.y));
	float cy = cos(radians(r.y));
	
	mat4 rotZ = mat4(
		cz, -sz, 0, 0,
		sz, cz,  0, 0,
		0,  0,   1, 0,
		0,  0,   0, 1
	);
	
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
	
	mat4 pivot = mat4(
		1, 0, 0, p.x,
		0, 1, 0, p.y,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	mat4 npivot = mat4(
		1, 0, 0, -p.x,
		0, 1, 0, -p.y,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	return vec4(v.x, v.y, 1, 1) * pivot * scale * rotZ * npivot * trans;
}

float map(float value, float min1, float max1, float min2, float max2) {
 	return min2 + (value - min1) * (max2 - min2) / (max1 - min1);
}

void main(){
	fTexCoords = vec2(map(aVert.x, -0.5, 0.5, 0, 1), map(aVert.y, -0.5, 0.5, 0, 1));
	fTexData = aTexData;
	vec4 pos = getTransformedPosition(aVert, aTranslation, aScale, aRotation, aPivot);
	gl_Position = uProjection * uView * pos;
}