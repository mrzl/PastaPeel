package pp;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Textfield;
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

    /**
     * The constructor of the ControlFrame
     *
     * @param parent the PApplet, which creates this Applet
     * @param width the width of the Frame
     * @param height the height of the Frame
     */
    public ControlFrame( PApplet parent, int width, int height ) {
        // casted, in order to call methods created in the PastaPeelPApplet class
        this.parent = ( PastaPeelPApplet ) parent;

        // setting with and height
        this.width = width;
        this.height = height;
    }

    /**
     * Being called, since this is a PApplet
     */
    public void setup() {
        cp = new ControlP5( this );
        cp.setBroadcast( false );

        cp.addButton( "CHANGE COLOR" ).setPosition( 30, 30 ).setSize( 80 ,40 ).setValue( 0.0f );
        cp.addButton( "TOGGLE GRID" ).setPosition( 30, 90 ).setSize( 80 ,40 ).setValue( 0.0f );
        cp.addButton( "CLEAR" ).setPosition( 120, 30 ).setSize( 80 ,40 ).setValue( 0.0f );

        cp.addTextfield( "savefilename" ).setPosition(20, 170).setSize(200, 20) .setFont(createFont("arial", 14)).setAutoClear(false);
        cp.addButton( "saveFile" ).setPosition( 240, 170 ).setSize( 60 ,20 ).setValue( 0.0f );

        cp.addTextfield( "loadfilename" ).setPosition(20, 210).setSize(200, 20) .setFont(createFont("arial", 14)).setAutoClear(false);
        cp.addButton( "loadFile" ).setPosition( 240, 210 ).setSize( 60 ,20 ).setValue( 0.0f );

        cp.setBroadcast( true );
    }

    /**
     * Draws the Applet
     */
    public void draw() {
        background(0);
        parent.drawCurrentColor(this);
    }

    /**
     * Callback method from the controlP5 library, whenever the 'saveFile' button is clicked
     */
    @SuppressWarnings( "unused" )
    public void saveFile() {
        String fileName = cp.get( Textfield.class, "savefilename" ).getText();
        parent.saveConfiguration(fileName + ".lml");
    }

    /**
     * Callback method from the controlP5 library, whenever the 'loadFile' button is clicked
     */
    @SuppressWarnings( "unused" )
    public void loadFile() {
        String fileName = cp.get( Textfield.class, "loadfilename" ).getText();
        parent.loadConfiguration(fileName);
    }

    /**
     *This is where all ControlEvents are being interpreted
     *
    *  @param e the ControlEvent containing all information about the event
     */
    @SuppressWarnings( "unused" )
    public void controlEvent( ControlEvent e ) {
        switch( e.getName() ){
            case "CHANGE COLOR":
                parent.nextColor();
                break;
            case "TOGGLE GRID":
                parent.toggleGrid();
                break;
            case "CLEAR":
                parent.clearLines();
                break;
        }
    }

    /**
     * Returns the width of the frame
     *
     * @return width the width of this frame window
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * returns the height of the frame
     * 
    * @return height the height of this frame window
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * static method to create a ControlFrame -> factory?
     *
     * @param _p the parent PApplet
     * @param name the name of the frame
     * @param width the width of the frame
     * @param height the height of the frame
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
