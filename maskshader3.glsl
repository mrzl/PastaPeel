#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

void main( void ) {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 position = ( gl_FragCoord.xy / resolution.xy ) + mouse / 4.0;

	vec3 color = vec3(0,0,0);
	
	color.x = cos(position.x * 12.0) + sin(mouse.y) + sin(time / 7.0);
	color.y = cos(position.y * 12.0) + sin(mouse.x) + cos(time / 4.0);
	color.z = cos(position.y * 15.0) + sin(mouse.y * 2.0) + sin(time / 2.0);
	
	gl_FragColor = vec4(color, 1);
	} else {
            gl_FragColor = vec4( 0, 0, 0, 0 );
        }
}