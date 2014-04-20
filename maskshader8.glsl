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

void main( void ) {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 p = gl_FragCoord.xy/resolution;
	p -= 0.5;
	p *= 20.;
	
	p += vec2(sin(p.x+time*2.), cos(p.y+time*2.))*.3;
	vec2 q = vec2(atan(p.x, -p.y)*10., length(p));
	p = mix(p,q,clamp(sin(time*.3)*2.+1., 0., 1.));
	//p = q;
	p += time*3.;
	p = mod(p, 2.);
	p = floor(p);
	
	float a = p.x+p.y == 1. ? 1. : 0.;
	

	gl_FragColor = vec4(a, a, a, 1.0);
} else {
        gl_FragColor = vec4( 0, 0, 0, 0 );
    }
}