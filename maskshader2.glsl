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
        vec2 position = ( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0  + vec2(0.0, 1.0);

        	float angle = atan(position.y/position.x)*1000.0 /tan(time)*sin(position.x/position.y);

        	angle = mod(angle, 500000.0) *0.13;

        	vec4 color = vec4(sin(angle), sin(angle), sin(angle), 1.0);

        	gl_FragColor = color;
    } else {
        gl_FragColor = vec4( 0, 0, 0, 0 );
    }


}