#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform float time;
uniform vec2 resolution;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

float angle(vec2 p){
	if(p.x<0.0)return atan(p.y/p.x)*180.0/3.1415926536+180.0;
	if(p.y>0.0)return atan(p.y/p.x)*180.0/3.1415926536;
	return atan(p.y/p.x)*180.0/3.1415926536+360.0;
}

void main( void ) {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 p=vec2((gl_FragCoord.x/(resolution.x/2.0)-1.0)*(resolution.x/resolution.y),
		     gl_FragCoord.y/(resolution.y/2.0)-1.0);
	float d=sqrt(p.x*p.x+p.y*p.y);
	float f=1.0;
	float a=mod(angle(p)*(1.0/f)+time*400.0,360.0);
	
	float c=cos(d*50.0+a*0.0524*f)*10.0;
	if(d<1.0)c=cos(d*50.0-a*0.0524*f)*10.0;
	if(d<0.5)c=cos(d*50.0+a*0.0524*f)*10.0;
	
	

	gl_FragColor = vec4( c,c,c, 1.0 );
} else {
        gl_FragColor = vec4( 0, 0, 0, 0 );
    }
}