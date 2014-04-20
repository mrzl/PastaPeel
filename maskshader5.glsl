#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

const float PI = 3.1415926536;
uniform float time;
uniform vec2 resolution;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

//

float hash(float x)
{
    return fract(sin(x) * 43758.5453) * 2.0 - 1.0;
}
vec2 hashPosition(float x)
{
	return vec2(hash(x), hash(x * 1.1))*2.0-1.0;
}

bool xor(bool a, bool b) {
	return (a && !b) || (!a && b);
}

float stripes(vec2 p) {
	float aa = 0.02;
	float xVal = + smoothstep(0.0-aa, 0.0+aa, p.x)
		         + (1.0 - smoothstep(0.5-aa, 0.5+aa, p.x))
		         + smoothstep(1.0-aa, 1.0+aa, p.x);
	return xVal-1.0;
}

void main(void)
{
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;

     if( maskColor.r > 0.9 ) {
	vec2 r = (gl_FragCoord.xy - 0.5*resolution.xy) / resolution.y;
	
	// move the center around
	r += vec2(0.35,0.0)*sin(0.92*sin(2.1*time)+0.2);
	r += vec2(0.0,0.5)*cos(0.45*time+0.3);	

	// polar coordinates
	float mag = length(r);
	float angle = atan(r.y,r.x)/PI;

	float side = 1.0;
	float val = 0.0;

		vec2 tunnel = vec2(0.3/mag, angle);
		tunnel += vec2(2.5*time, 0.0);//forward speed and angular speed		
		val = stripes(mod(tunnel, side));

	// white and black colors
	vec3 color = mix(vec3(1.0,1.0,1.0), vec3(0.0,0.0,0.0), val);
	
	// the light ring that goes into the tunnel
	float signalDepth = pow(mod(1.5 - 0.9*time, 4.0),2.0);
	float s1 = signalDepth*0.9;
	float s2 = signalDepth*1.1;
	float dd = 0.05;
	color += 0.5*smoothstep(s1-dd,s1+dd, mag)*(1.0-smoothstep(s2-dd,s2+dd,mag))*vec3(1.0,1.0,0.5);
	
	color -= (1.0-smoothstep(0.0, 0.2, mag)); // shadow at the end of the tunnel
	color *= smoothstep( 1.8, 0.15, mag ); // vignette
	gl_FragColor = vec4(color, 1.0);
	} else {
            gl_FragColor = vec4( 0, 0, 0, 0 );
        }
}