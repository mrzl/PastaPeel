#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER

uniform vec2 resolution;
uniform float time;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

// This is the speed factor for the animation
// Ex: could you make it dependant on the mouse position?
const float freq = 0.001;

// GLSL supports the use of custom functions in shaders
// but they HAVE TO be declared before the main() function
vec4 getColor( float dist ) {

	// We use the time value to compute a variable oscillating between 0.0 and 2.0 over time
 	float oscillation = sin( time* 1000 * freq ) + 1.0;

 	// A nice blue tint
    float red   = 0.1;
    float green = 0.8;
    float blue  = 0.9;

    // Compute the lower and higher threshold for our smoothstep() function
    float low = 1.0 - oscillation;
    float high = 1.0 - (oscillation * 0.1);

    // Drawing our smooth circle again (now with parameters that change over time)
    float alpha = smoothstep( low, high, 1.0 - dist );

 	vec4 color = vec4(red, green, blue, alpha);

 	return color;
}

void main(void)
{
    vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if(maskColor.r > 0.9)
    {

	    // Coordinates of the fragment in the (-1,-1 ; 1, 1)
 	    vec2 coord = vec2 (-1.0 + 2.0 * gl_FragCoord.x / resolution.x, 1.0 - 2.0 * gl_FragCoord.y / resolution.y) ;

 	    // How far is the current fragment from the center?
 	    float distance = length( coord.xy );


 	    // We ask getColor() to compute the color for the current distance
 	    gl_FragColor = getColor(distance);
    } else {
        gl_FragColor = vec4( 0, 0, 0, 0 );
    }
}