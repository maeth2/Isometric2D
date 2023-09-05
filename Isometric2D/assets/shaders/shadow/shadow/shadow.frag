#version 330 core
#define PI 3.14

in vec2 fTexCoords;

uniform sampler2D uTexture;
uniform float uRadius;
uniform vec3 uColor;
uniform float uIntensity;

out vec4 color;

float sample(vec2 coord, float r) {
	return step(r, texture2D(uTexture, coord).r);
}

void main(){
	//Converting Rectangular to Polar 
	vec2 coords = fTexCoords * 2.0 - 1.0;
	float theta = atan(coords.y, coords.x);
	float r = length(coords);
	float coord = (theta + PI) / (2.0 * PI);
	vec2 tc = vec2(coord, 0);
	float center = sample(tc, r);
	
	//Guasian Blur
	float blur = (1/uRadius) * smoothstep(0, 1, r); 
	float sum = 0.0;
	sum += sample(vec2(tc.x - 4.0*blur, tc.y), r) * 0.05;
	sum += sample(vec2(tc.x - 3.0*blur, tc.y), r) * 0.09;
	sum += sample(vec2(tc.x - 2.0*blur, tc.y), r) * 0.12;
	sum += sample(vec2(tc.x - 1.0*blur, tc.y), r) * 0.15;
	
	sum += center * 0.16;
	
	sum += sample(vec2(tc.x + 1.0*blur, tc.y), r) * 0.15;
	sum += sample(vec2(tc.x + 2.0*blur, tc.y), r) * 0.12;
	sum += sample(vec2(tc.x + 3.0*blur, tc.y), r) * 0.09;
	sum += sample(vec2(tc.x + 4.0*blur, tc.y), r) * 0.05;

 	color = vec4(uColor, sum * smoothstep(1, 0, r) * uIntensity);
}
