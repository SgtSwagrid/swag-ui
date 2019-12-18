#version 400 core

in vec2 texmap_;

out vec4 pixel;

uniform vec4 colour;
uniform sampler2D sampler;
uniform bool textured;

void main(void) {
	
	pixel = colour/255;
	if(textured) {
		pixel *= texture(sampler, texmap_);
	}
}