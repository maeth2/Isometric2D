#version 330 core

in vec2 fTexCoords;
in vec2 fPosition;

uniform sampler2D uTexture;
uniform sampler2D uShadow;
uniform vec3 uAmbient;
uniform bool uHasShadow;

out vec4 color;

struct Light{
	vec2 position;
	vec3 color;
	float radius;
	bool castShadow;
};

uniform Light uLights[100];
uniform int uLightNum;

void main(){
	vec3 finalColor = vec3(0);
	for(int i = 0; i < uLightNum; i++){
		Light light = uLights[i];
		if(!light.castShadow || !uHasShadow){
			float distance = length(light.position - fPosition);
			float attenuation = clamp(1 - distance / light.radius, 0, 1);
			float intensity = 1;
			vec3 lightColor = light.color * attenuation * attenuation * intensity;
			finalColor += lightColor;
		}
	}
	vec4 screenColor = texture(uTexture, fTexCoords);
	vec4 finalScreenColor = screenColor + vec4(finalColor, 1);
	if(uHasShadow){
		vec4 shadowColor = texture(uShadow, fTexCoords);
		finalScreenColor += shadowColor;
	}
	
	finalScreenColor *= vec4(uAmbient, 1);
	color = finalScreenColor;
}
