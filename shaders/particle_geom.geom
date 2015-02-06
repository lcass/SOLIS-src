#version 330
//uses an 8 by 8 spritesheet make sure you use it!

layout (points) in;
layout (triangle_strip) out;
layout (max_vertices = 4) out;
uniform vec2 dimension;
uniform float size;
uniform float time;
uniform float decay;
uniform vec2 spawn;
in VertexData{
	float decay_point;
}Vert[1];
out vec2 texcoord;
void main(){
	if(gl_in[0].gl_Position.x > 1.1 || gl_in[0].gl_Position.x < -1.1 || gl_in[0].gl_Position.y > 1.1 || gl_in[0].gl_Position.x < -1.1){
	}
	else{
	if( Vert[0].decay_point < decay ){
   vec2 size_adjust = vec2(size/dimension.x,size/dimension.y);
   float eigth = 0.125;
    gl_Position = vec4(gl_in[0].gl_Position.x,gl_in[0].gl_Position.y,1,1);
    texcoord = vec2(gl_in[0].gl_Position.z,gl_in[0].gl_Position.w + eigth);
    EmitVertex();
    gl_Position = vec4(gl_in[0].gl_Position.x,gl_in[0].gl_Position.y + size_adjust.y,1,1);
    texcoord = vec2(gl_in[0].gl_Position.z,gl_in[0].gl_Position.w);
    EmitVertex();
    gl_Position = vec4(gl_in[0].gl_Position.x + size_adjust.x,gl_in[0].gl_Position.y,1,1);
    texcoord = vec2(gl_in[0].gl_Position.z + (1/8),gl_in[0].gl_Position.w + (1/8));
    EmitVertex();
    gl_Position = vec4(gl_in[0].gl_Position.x + size_adjust.x,gl_in[0].gl_Position.y + size_adjust.y,1,1);
    texcoord = vec2(gl_in[0].gl_Position.z + eigth,gl_in[0].gl_Position.w);
    EmitVertex();

    EndPrimitive();
        }
        }
}