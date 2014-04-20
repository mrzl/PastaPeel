package pp.panke;

import pp.gui.PankeControls;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.opengl.PShader;

import java.util.*;

/**
 * Author: mrzl
 * Date: 08.04.14
 * Time: 22:04
 * Project: PastaPeel
 */
public class PankePastaPeel extends PApplet {
    public static final int FILL_COLOR = 255;

    private PImage placeholderImage;

    private LinkedHashMap<PShader, PGraphics> shaders;
    int currentDrawingBuffer = 0;

    public boolean isDrawingMode = false;
    public boolean editMode = true;
    public boolean initBuffers = false;

    private PankeControls controls;

    public void init() {
        frame.removeNotify();
        frame.setUndecorated( true );
        frame.addNotify();
        super.init();
    }

    public void setup() {
        size( 1024, 768, P2D );
        frameRate( 500 );

        controls = new PankeControls( this );
        controls.pack();
        controls.setVisible( true );

        setupBuffers();

        placeholderImage = loadImage( "leaves.jpg" );
        background( 0 );

        //frame.setLocation( 1920, 0 );
        frame.setLocation( 0, 0 );
    }

    public void setupBuffers() {
        shaders = new LinkedHashMap<PShader, PGraphics>();
        if ( !editMode ) {
            addShader( "maskshader2.glsl" );
            addShader( "maskshader4.glsl" );
            addShader( "maskshader5.glsl" );
            addShader( "maskshader6.glsl" );
            addShader( "maskshader7.glsl" );
            addShader( "maskshader8.glsl" );
            addShader( "maskshader9.glsl" );
            addShader( "maskshader10.glsl" );
            addShader( "maskshader11.glsl" );
            addShader( "maskshader12.glsl" );
        } else {
            addShaderAndLoadBuffer( "maskshader2.glsl", "buffer1.png" );
            addShaderAndLoadBuffer( "maskshader4.glsl", "buffer3.png" );
            addShaderAndLoadBuffer( "maskshader5.glsl", "buffer4.png" );
            addShaderAndLoadBuffer( "maskshader6.glsl", "buffer5.png" );
            addShaderAndLoadBuffer( "maskshader7.glsl", "buffer6.png" );
            addShaderAndLoadBuffer( "maskshader8.glsl", "buffer7.png" );
            addShaderAndLoadBuffer( "maskshader9.glsl", "buffer8.png" );
            addShaderAndLoadBuffer( "maskshader10.glsl", "buffer9.png" );
            addShaderAndLoadBuffer( "maskshader11.glsl", "buffer0.png" );
            addShaderAndLoadBuffer( "maskshader12.glsl", "buffer2.png" );
        }
    }

    private void addShaderAndLoadBuffer( String shaderFileName, String bufferFileName ) {
        float widthWindow = width;
        float heightWindow = height;

        PShader maskShader1 = loadShader( shaderFileName );
        PImage maskImage2 = loadImage( bufferFileName );
        PGraphics maskBuffer1 = createGraphics( width, height, P2D );
        maskBuffer1.noSmooth();
        maskBuffer1.beginDraw();
        maskBuffer1.noStroke();
        maskBuffer1.image( maskImage2, 0, 0, maskBuffer1.width, maskBuffer1.height );
        maskBuffer1.endDraw();
        maskShader1.set( "mask", maskBuffer1 );
        maskShader1.set( "resolution", widthWindow, heightWindow );
        shaders.put( maskShader1, maskBuffer1 );
    }

    private void addShader( String s ) {
        float widthWindow = width;
        float heightWindow = height;

        PShader maskShader1 = loadShader( s );
        PGraphics maskBuffer1 = createGraphics( width, height, P2D );
        maskBuffer1.noSmooth();
        maskBuffer1.beginDraw();
        maskBuffer1.noStroke();
        maskBuffer1.endDraw();
        maskShader1.set( "mask", maskBuffer1 );
        maskShader1.set( "resolution", widthWindow, heightWindow );
        shaders.put( maskShader1, maskBuffer1 );
    }

    public void draw() {
        background(0);
        if ( initBuffers ) {
            setupBuffers();
            initBuffers = false;
        }

        List<PGraphics> ps = new ArrayList<PGraphics>( shaders.values() );
        for ( Map.Entry<PShader, PGraphics> entry : shaders.entrySet() ) {
            PShader s = entry.getKey();
            s.set( "time", ( float ) ( ( float ) millis() / 1000.0 ) );
            shader( s );
            image( placeholderImage, 0, 0, width, height );
        }

        if ( isDrawingMode ) {
            try {
                PGraphics currentGraphics = ps.get( currentDrawingBuffer );
                currentGraphics.beginDraw();
                currentGraphics.fill( FILL_COLOR );
                currentGraphics.ellipse( mouseX, mouseY, 10, 10 );
                currentGraphics.endDraw();
            } catch ( ArrayIndexOutOfBoundsException e ) {
                System.out.println( "Wrong currentDrawingBuffer." );
            }
        }
    }

    public void shuffle() {
        List<PGraphics> pg = new ArrayList<PGraphics>( shaders.values() );

        Collections.shuffle( pg );

        int index = 0;
        for ( PShader k : shaders.keySet() ) {
            k.set( "mask", pg.get( index ) );
            index++;
        }
    }

    public void mousePressed() {
    }

    public void keyPressed() {
        switch ( key ) {
            case '1':
                currentDrawingBuffer = 0;
                break;
            case '2':
                currentDrawingBuffer = 1;
                break;
            case '3':
                currentDrawingBuffer = 2;
                break;
            case '4':
                currentDrawingBuffer = 3;
                break;
            case '5':
                currentDrawingBuffer = 4;
                break;
            case '6':
                currentDrawingBuffer = 5;
                break;
            case '7':
                currentDrawingBuffer = 6;
                break;
            case '8':
                currentDrawingBuffer = 7;
                break;
            case '9':
                currentDrawingBuffer = 8;
                break;
            case '0':
                currentDrawingBuffer = 9;
                break;
            case 'd':
                this.isDrawingMode = !this.isDrawingMode;
                break;
            case 's':
                saveAllBuffers();
                break;
            case 'f':
                shuffle();
        }
    }

    public void saveAllBuffers() {
        int index = 0;
        System.out.println( "Saving." );
        List<PGraphics> ps = new ArrayList<PGraphics>( shaders.values() );
        for ( PGraphics g : ps ) {
            g.save( "buffer" + index + ".png" );
            index++;
        }
    }

}
