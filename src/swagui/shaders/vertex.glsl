#version 400 core

in vec2 vertex;
in vec2 texmap;

out vec2 texmap_;

uniform mat4 view;
uniform mat4 transform;

void main(void) {
	
	gl_Position = view * transform * vec4(vertex, 0.0, 1.0);
	texmap_ = texmap;
}