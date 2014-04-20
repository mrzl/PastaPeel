#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

#define PROCESSING_TEXTURE_SHADER
#define M_PI 3.1415926535897932384626433832795

uniform float time;
uniform vec2 resolution;
uniform sampler2D mask;

uniform vec2 texOffset;
varying vec4 vertColor;
varying vec4 vertTexCoord;

float dist2intensity(float dist)
{	
   float d=mod(dist,100.0);
   return pow(1.0/d,2.0);
}


void main( void )
{
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
	vec2 position = gl_FragCoord.xy;
	
	float offsetx=time*100.0;
	
	float at= 0.0;
	float bt=  M_PI*120.0/180.0;
	float ct= -M_PI*120.0/180.0;
	
	float ax= cos(at)*position.x-sin(at)*position.y;
	float bx= cos(bt)*position.x-sin(bt)*position.y;
	float cx= cos(ct)*position.x-sin(ct)*position.y;

	float ai= dist2intensity(ax-offsetx);
	float bi= dist2intensity(bx-offsetx);
	float ci= dist2intensity(cx-offsetx);
	float intensity = ai+bi+ci;
	vec4 color = vec4(1.0,1.0,1.0,1.0);
	gl_FragColor = vec4(vec3(color*intensity), 1.0 );
	} else {
            gl_FragColor = vec4( 0, 0, 0, 0 );
        }
}