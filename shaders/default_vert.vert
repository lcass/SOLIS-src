
attribute vec2 position;
attribute vec2 texin;
uniform vec2 transform;
uniform float timein;
uniform float zoom;
uniform vec2 rotpos;
uniform float light;
uniform int bloomval;
varying vec2 vecdata;
varying float time;
varying float bloom;
varying float lightlines;
varying vec3 color_out;
uniform vec3 color_in;

uniform vec2 dimension;
uniform float rotation;
vec2 convertback(vec2 data){
float tx = data.x * dimension.x;
float ty = data.y * dimension.y;
vec2 stage1 = vec2(tx,ty);
tx = stage1.x + dimension.x;
ty = stage1.y + dimension.y;
return vec2(tx,ty);
}
vec2 convertto(vec2 data){
float tx = data.x ;
float ty = data.y;
vec2 stage1 = vec2(tx - dimension.x,ty - dimension.y);
vec2 stage2 = vec2((stage1.x )/dimension.x,(stage1.y )/dimension.y);
return stage2;
}
void main(){
	color_out = color_in;
   time = timein;
   bloom = bloomval;
   lightlines=light;
   if(zoom <= 0){//if its negative then its not fun :(
         zoom = 1;
   }
   
   vec4 offset;
   if(rotation != 0){
	  
	   rotpos *=2;
	  
	   vec2 temp = convertback(vec2(position.x,position.y));
	   offset = vec4(temp.x - rotpos.x,temp.y - rotpos.y,1,1);
	   mat4 RotationMatrix = mat4( cos( rotation ), sin( rotation ), 0.0, 0.0,
	             -
	             sin( rotation ),  cos( rotation ), 0.0, 0.0,
	                      0.0,           0.0, 1.0, 0.0,
	                 0.0,           0.0, 0.0, 1.0 );
	                
	   offset *= RotationMatrix;
	   temp = convertto(vec2(offset.x+ rotpos.x,offset.y + rotpos.y));
	   offset = vec4((temp.x ) + transform.x ,temp.y + transform.y,1,1);
   }else{
   	offset = vec4((position.x+transform.x) * zoom,(position.y+transform.y) *zoom,1,1);
   }
   gl_Position = offset;
   vecdata = texin;


}