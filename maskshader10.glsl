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

float rand(vec2 co) {
	float a = 1e3;
	float b = 1e-3;
	float c = 1e5;
	return fract(sin((co.x+co.y*a)*b)*c);
}

void main( void ) {
    vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 position = ( gl_FragCoord.xy / resolution.xy );
	
	float r = rand(floor(position*vec2(40.0, 40.0)));
	float progress = abs(1.0 + fract(time/20.0)*2.0 * -1.0)*30.0;
	float color = r * (0.25 * sin(progress * (r * 5.0) + 720.0 * r) + 0.75);
	
	

	gl_FragColor = vec4(vec3(color), 1.0);
} else {
        gl_FragColor = vec4( 0, 0, 0, 0 );
    }
}