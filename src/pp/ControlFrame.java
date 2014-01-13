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
    // the ControlP5 instance.
    private ControlP5 cp;

    // the parent class.
    private PastaPeelPApplet parent;

    private int width, height;

    /*
    The constructor of the ControlFrame

    @param PApplet parent the PApplet, which creates this Applet
    @param int width the width of the Frame
    @param int height the height of the Frame
     */
    public ControlFrame( PApplet parent, int width, int height ) {
        // casted, in order to call methods created in the PastaPeelPApplet class
        this.parent = ( PastaPeelPApplet ) parent;

        // setting with and height
        this.width = width;
        this.height = height;
    }

    /*
    Being called, since this is a PApplet
     */
    public void setup() {
        cp = new ControlP5( this );
        cp.setBroadcast( false );
    }

    /*
    draws the Applet
     */
    public void draw() {
        background(0);
    }

    /*
    This is where all ControlEvents are being interpreted

    @param ControlEvent e the ControlEvent containing all information about the event
     */
    public void controlEvent( ControlEvent e ) {
        switch( e.getName() ){
            case "hey":
                break;
        }
    }

    /*
    Returns the width of the frame

    @return width
     */
    public int getWidth() {
        return this.width;
    }

    /*
    returns the height of the frame

    @return height
     */
    public int getHeight() {
        return this.height;
    }

    /*
    static method to create a ControlFrame -> factory?

    @param PApplet _p the parent PApplet
    @param String name the name of the frame
    @param int width the width of the frame
    @param int height the height of the frame
     */
    static ControlFrame createControlFrame( PApplet _p, String name, int width, int height ) {
        // creates a new frame with the passed frame name
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
