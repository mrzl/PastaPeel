package pp;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import processing.core.PApplet;

import java.awt.*;

/**
 * Author: mrzl
 * Date: 12.01.14
 * Time: 22:46
 * Project: PastaPeel
 */
public class ControlFrame extends PApplet {
    private ControlP5 cp;
    private PastaPeelPApplet parent;

    private int width, height;

    public ControlFrame( PApplet parent, int width, int height ) {
        this.parent = ( PastaPeelPApplet ) parent;
        this.width = width;
        this.height = height;
    }

    public void setup() {
        cp = new ControlP5( this );
        cp.setBroadcast( false );
    }

    public void draw() {
        background( 0 );
    }

    public void controlEvent( ControlEvent e ) {
        switch( e.getName() ){
            case "hey":
                break;
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    static ControlFrame createControlFrame( PApplet _p, String name, int width, int height ) {
        Frame f = new Frame( name );
        ControlFrame p = new ControlFrame( _p, width, height );
        f.add( p );
        p.init();
        f.setTitle( name );
        f.setSize( p.getWidth(), p.getHeight() );
        f.setResizable( false );
        f.setVisible( true );
        return p;
    }
}
