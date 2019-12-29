#version 400 core

in vec2 vertex;
in vec2 texmap;

out vec4 colour;
out vec2 texmap_;

uniform mat4 view;
uniform mat4 transform;
uniform vec4 colours[4];

void main(void) {
	
	gl_Position = view * transform * vec4(vertex, 0.0, 1.0);
	texmap_ = texmap;
	
	if(vertex == vec2(-0.5, -0.5)) colour = colours[0];
	else if(vertex == vec2(-0.5, 0.5)) colour = colours[1];
	else if(vertex == vec2(0.5, 0.5)) colour = colours[2];
	else if(vertex == vec2(0.5, -0.5)) colour = colours[3];
	else colour = vec4(0.0, 1.0, 1.0, 1.0);
}