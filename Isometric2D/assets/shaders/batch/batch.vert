#version 330 core

layout(location=0) in vec2 aVert;
layout(location=1) in vec2 aTranslation;
layout(location=2) in vec2 aScale;
layout(location=3) in vec2 aRotation;
layout(location=4) in vec4 aTexData;

out vec2 fTexCoords;
out vec4 fTexData;

uniform mat4 uProjection;
uniform mat4 uView;


vec4 getTransformedPosition(vec2 v, vec2 t, vec2 s, vec2 r){
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
	
	return scale * rotZ * vec4(v.x, v.y, 1, 1) * trans;
}

void main(){
	fTexCoords =  vec2((aVert.x+1.0)/2.0, (aVert.y+1.0)/2.0);
	fTexData = aTexData;
	vec4 pos = getTransformedPosition(aVert, aTranslation, aScale, aRotation);
	gl_Position = uProjection * uView * pos;
}