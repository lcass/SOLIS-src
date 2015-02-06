uniform sampler2D texture;
uniform float alpha;
in vec2 texcoord;
void main(){
	gl_FragColor = vec4(texture2D(texture,texcoord).rgb,alpha);
}