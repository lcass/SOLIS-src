#version 330
uniform float alpha;
uniform float time;
uniform vec2 spawn;
uniform float decay;
uniform float velocity = 1;
uniform vec2 dimension;
uniform vec2 transformation = vec2(0,0);
uniform float size;
in vec4 vecin;
in vec3 texin;
out VertexData{
	float decay_point;
}Vert;
void main(){
	float x_move,y_move,negate_x,negate_y;
	x_move = (((time-(texin.z))* (vecin.x - spawn.x))) * velocity * vecin.z;
	y_move = (((time-(texin.z)) * (vecin.y - spawn.y))) * velocity * vecin.z;
	x_move/= 10000;
	y_move/= 10000;
   	gl_Position = vec4(vecin.x + x_move + transformation.x,vecin.y + y_move + transformation.y,texin.x,texin.y);
   	
   Vert.decay_point = ((vecin.w) * (time - texin.z))/20;
}