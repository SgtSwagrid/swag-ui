#version 400 core

in vec4 colour;
in vec2 texmap_;

out vec4 pixel;

uniform sampler2D sampler;
uniform bool textured;

void main(void) {
	
	pixel = colour/255.0;
	if(textured) {
		pixel *= texture(sampler, texmap_);
	}
}