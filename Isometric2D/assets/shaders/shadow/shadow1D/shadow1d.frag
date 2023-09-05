#version 330 core
#define PI 3.14

in vec2 fTexCoords;

uniform sampler2D uTexture;
uniform float uRadius;

out vec4 color; 

const float THRESHOLD = 0.75;

void main(){
	float theta = 2 * PI * fTexCoords.x;
	float distance = 1.0;
	for(int i = 0; i < uRadius; i++){
		float r = i / uRadius;
		vec2 coord = (vec2(-r * cos(theta), -r * sin(theta)) + 1) / 2;
		vec4 data = texture2D(uTexture, coord);
		
		if(data.a >= THRESHOLD){
			distance = r;
			break;
		}
	}
	color = vec4(vec3(distance), 1);
}