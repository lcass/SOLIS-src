uniform sampler2D bgl_RenderedTexture;
varying vec2 vecdata;
varying float bloom;
varying vec3 color_out;//only used for line_frag
varying float lightlines;
varying float time;

void main(){
	vec4 tex = texture2D(bgl_RenderedTexture,vecdata);
    if(tex.rgb == vec3(1.0,0,1.0)){
         discard;
    }
    else{
    gl_FragColor = tex;
    }
    
    
  }
    
   