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


mat4 createTransformationMatrix(vec2 s, vec2 r){
	float sz = sin(radians(r.x + 180));
	float cz = cos(radians(r.x + 180));

	mat4 rotZ = mat4(
		cz, -sz, 0, 0,
		sz, cz,  0, 0,
		0,  0,   1, 0,
		0,  0,   0, 1
	);
	
	mat4 scale = mat4(
		s.x, 0, 0, 0,
		0, s.y, 0, 0,
		0, 0, 1, 0,
		0, 0, 0, 1
	);
	
	return rotZ * scale;
}

void main(){
	fTexCoords =  vec2((aVert.x+1.0)/2.0, (aVert.y+1.0)/2.0);
	fTexData = aTexData;
	mat4 transformation = createTransformationMatrix(aScale, aRotation);
	vec4 pos = transformation * vec4(aVert, 0.0, 1.0) + vec4(aTranslation, 0, 0);
	gl_Position = uProjection * uView * pos;
}