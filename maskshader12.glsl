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
 
float rings(vec2 p)
{
  return cos(length(p - vec2(cos(time), sin(time))) * 150.0 ) ;
}
 
void main() {
vec4 maskColor = texture2D(mask, vec2(vertTexCoord.s, 1.0 - vertTexCoord.t)).rgba;
    if( maskColor.r > 0.9 ) {
  vec2 pos = (gl_FragCoord.xy*2.0 - resolution) / resolution.y;
 
  gl_FragColor = vec4(rings(pos), rings(pos), rings(pos), 1.0);
  } else {
          gl_FragColor = vec4( 0, 0, 0, 0 );
      }
}