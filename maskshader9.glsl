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


float sq(float a){
	return a*a;
}

void main( void ) {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	float zoom = 0.01;
	float dist = (sq(gl_FragCoord.x)+sq(gl_FragCoord.y))*zoom;
	float dist2 = ((sin(dist+time)-0.9)*115.);
	gl_FragColor = vec4(dist2,dist2,dist2,1);
	} else {
            gl_FragColor = vec4( 0, 0, 0, 0 );
        }
}