#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform sampler2D texture;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

uniform vec2 resolution;
uniform float time;

// This is the speed factor for the animation
// Ex: could you make it dependant on the mouse position?
const float freq = 0.001;

float sq(float a){
	return a*a;
}

void main() {
  vec4 texColor = texture2D(texture, vertTexCoord.st).rgba;
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
  } else if(maskColor.g > 0.9 ) {
	float zoom = 0.01;
	float dist = (sq(gl_FragCoord.x)+sq(gl_FragCoord.y))*zoom;
	float dist2 = ((sin(dist+time)-0.9)*115.);
	gl_FragColor = vec4(dist2,dist2,dist2,1);
  } else {
	gl_FragColor = vec4(0, 0, 0, 0);
  }
}