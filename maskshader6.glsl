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

// Choose a preset. (from 0 to 3, for now)
int presetNo = 1;

// PARAMETERS
float numTile = 4.0; // number of tiles in vertical direction
float maxCol = 1.0;
float minCol = 0.25;
const float numFrames = 4.0; // number of frames to be used in motion blur
// change these at presets
float numDiv;
float overallSpeed;
bool constantSpeed; // if true rotates continuously w/o stopping
float stopAngle; // if constantSpeed is false, stop every this angle

// hyberbolic tangent function
float tanh(float x) {
	return (exp(x)-exp(-x))/(exp(x)+exp(-x));
}

// modulus function for integers
int imode(int n, int m) {
    return n-m*(n/m);
}

// returns the color of a division
float colN(int n) {
    return minCol + float(n+1)*(maxCol-minCol)/float(numDiv);
}

// anti-alias(?) between divisions
float colorVal(float angle) {
    float antiAlias = 0.03; // higher smoother 

    float divAngle = 2.0*PI / numDiv; // angle per division
    int n = int(angle/divAngle); // the division number. 0 -> numDiv - 1
    float smoothv = smoothstep( float(n)*divAngle-antiAlias, float(n)*divAngle+antiAlias, angle-0.5*divAngle );
	//return colN(n);
    return colN(n) + ( colN(imode(n+1,int(numDiv)))-colN(n))*smoothv;
}

 
void main() {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 uv = gl_FragCoord.xy / resolution.y;
	//vec2 mo = 0.0 / resolution.y;
	
    float side = 1.0/numTile;
	
    vec2 pos = mod(uv, vec2(side)) - vec2(0.5*side); // generate tiles

	float i = floor(numTile*uv.x);
    float j = floor(numTile*uv.y);
	
    float im = i-2.*(i/2.);
    float jm = j-2.*(j/2.);
    float im2 = im*2.-1.; // for alternating directions 
    float jm2 = jm*2.-1.;
    float ijm = i+j-2.*((i+j)/2.);

    float angle = atan(pos.x, pos.y);
	float t = time;
	
	// change the phase and speed of different tiles individually as function of their coordinates	
	if(presetNo==0) { // 
		angle += - i*PI + j*PI; // adjust phase as a function of tile coordinate
		overallSpeed = 1.5;
		t *= overallSpeed;
		numDiv = 4.0;
		constantSpeed = false;
		stopAngle = PI/4.0;		
	} else if(presetNo == 1) { // simplest
		angle += 0.0;
		overallSpeed = 2.0;
		t *= overallSpeed;
		numDiv = 2.0;
		constantSpeed = true;
		stopAngle = PI/4.0;
	} else if(presetNo == 2) { // 
		angle += mod(j,2.0)*PI;
		overallSpeed = mod(i,2.0)*6.0-3.0; // adjust the rotation speed of an individual tile
		t *= overallSpeed;
		numDiv = 3.0;
		constantSpeed = false;
		stopAngle = PI/6.0;
	} else if(presetNo == 3) { // 
		angle += 0.0*PI;
		overallSpeed = (i-3.5)*(j-1.5)*2.0;
		t *= overallSpeed;
		numDiv = 4.0;
		constantSpeed = false;
		stopAngle = PI/2.0;
	}	
	
	// motion blur
	float col = 0.0;
	float inNumFrames = 1.0/numFrames;
	for(float m=0.0; m<numFrames; m+=1.0) {
		float t1;
		if(constantSpeed) {
			t1 = t;
		} else {
			t1 = (floor(t + 0.02*m) + smoothstep(0.1, 0.9, fract(t + 0.02*m
)) )*stopAngle;
		}
		float an = mod(angle + t1, 2.0*PI);
		//col += inNumFrames*colorVal( an ); // each frame has the same weight in motion blur
		col += colorVal( an )*(m+1.0)/(numFrames*(numFrames+1.0)*0.5); // frames have different weights
	}
	
	// vignette
	vec2 vig = (gl_FragCoord.xy - 0.5*resolution.xy) / resolution.y;
	col *= smoothstep( 1.8, 0.5, length(vig) );
    gl_FragColor = vec4( col, col, col, 1.0);
    } else {
            gl_FragColor = vec4( 0, 0, 0, 0 );
        }
}