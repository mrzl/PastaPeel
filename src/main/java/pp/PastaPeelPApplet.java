package pp;

import deadpixel.keystone.CornerPinSurface;
import deadpixel.keystone.Keystone;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.opengl.PGraphics2D;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Marcel on 12.01.14.
 */
public class PastaPeelPApplet extends PApplet {

    // class that specifies the grid
    private Grid grid;
    // class containing all previously drawn lines
    private LinePool linePool;
    // class that holts all different tape colors
    private ColorChooser colorChooser;
    private int selectedColorIndex;
    // filename of the exported PDF
    private String currentFileNamePDF;
    // filename of the exported PNG
    private String currentFileNamePNG;

    private ControlFrame controlFrame;
    private boolean drawGrid;

    private PGraphics buffer;
    private Keystone ks;
    private CornerPinSurface surface;

    /**
     * Callback method from processing. This is called once, when the program is started
     */
    @Override
    public void setup() {
        size(800, 800, P2D);
        buffer = createGraphics( width, height, P2D );

        grid = new Grid( this, buffer, 20, 20 );
        linePool = new LinePool( this, buffer );
        colorChooser = new ColorChooser( this );
        selectedColorIndex = 0;
        drawGrid = true;

        controlFrame = ControlFrame.createControlFrame( this, "Controls", 400, 600 );


        ks = new Keystone( this );
        surface = ks.createCornerPinSurface( width, height, 30 );

    }

    /**
     * Callback method from processing. This is called as often as possible.
     */
    @Override
    public void draw() {
        background( 0 );
        buffer.beginDraw();
        buffer.background( 127 );
        buffer.endDraw();

        if( this.frame != null ){
            frame.setTitle( ( int ) ( this.frameRate ) + "fps" );
        }

        if( drawGrid ) {
            grid.draw();
        }
        PVector surfaceMouse = surface.getTransformedMouse();
        grid.drawActiveBox( surfaceMouse );

        linePool.drawLines( grid );

        surface.render( buffer );
    }

    /**
     * Callback method from processing, whenever a mouse key is pressed
     */
    @Override
    public void mousePressed() {
        // only draw lines if in presentation mode- this avoids accidentally drawn lines
        if( !ks.isCalibrating() ) {
            PVector surfaceMouse = surface.getTransformedMouse();
            linePool.mousePressed( grid, colorChooser.getColor( selectedColorIndex ), surfaceMouse );
        }
    }

    /**
     * Callback method from processing, whenever a key is pressed
     */
    @Override
     public void keyPressed() {
        String lastLinesFileName;
        switch( key ) {
            case 's':
                savePDF();
                savePNG();
                System.out.println( "Saved file: " + currentFileNamePDF + " + " + currentFileNamePNG );
                break;
            case 'e':
                saveConfiguration("auto_export.lml");
                break;
            case 'l':
                lastLinesFileName = "last_lines.csv";
                linePool.loadFromFile( lastLinesFileName );
                System.out.println( "Loaded lines from " + lastLinesFileName );
                break;
            case 'c':
                ks.toggleCalibration();
                break;
        }
    }

    /**
     * Saves the currently drawn lines to a file.
     *
     * @param lastLinesFileName the filename of the .lml file
     */
    protected void saveConfiguration( String lastLinesFileName ) {
        lastLinesFileName = "saved" + File.separator + lastLinesFileName;
        linePool.saveToFile( lastLinesFileName );
        System.out.println( "Saved lines to " +  lastLinesFileName);
    }

    /**
     * Loads a file containing a set of lines.
     *
     * @param loadLinesFileName the file name of the .lml file
     */
    protected void loadConfiguration( String loadLinesFileName ) {
        this.clearLines();
        linePool.loadFromFile( loadLinesFileName );
        System.out.println( "Loaded lines from " + loadLinesFileName );
    }

    /**
     * Deletes all currently drawn lines from the LinePool
     */
    protected void clearLines() {
        linePool.clear();
    }
    /**
     * Saves a PDF with the current output.
     */
     private void savePDF() {
         currentFileNamePDF = "pasta_peel-" + getTimeStamp() + ".pdf";
         linePool.saveLinesToPDF( currentFileNamePDF, grid );
    }

    /**
     * Saves a PNG with the current output.
     */
    private void savePNG() {
        currentFileNamePNG = "pasta_peel-" + getTimeStamp() + ".png";
        saveFrame( currentFileNamePNG );
    }

    /**
     * creates a String encoding the current timestamp

     @return String the String containing the current timestamp
     */
    public String getTimeStamp() {
        Calendar now = Calendar.getInstance();
        return String.format( "%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now );
    }

    /**
     * The main function. starting this PApplet.
     */
    public static void main( String[] args ) {
        // starting this PApplet
        PApplet.main( new String[ ] { "pp.PastaPeelPApplet" } );
    }

    /**
     * Draws a box with the currently selected color to the passed PApplet
     *
     * @param controlWindow the control-applet.
     */
    public void drawCurrentColor( PApplet controlWindow ) {
        controlWindow.stroke( 255 );
        controlWindow.fill( colorChooser.getColor( selectedColorIndex ) );
        controlWindow.rect( 140, 30, 50, 50 );
    }

    /**
     * Selects the next color
     */
    protected void nextColor() {
        selectedColorIndex++;
        selectedColorIndex %= colorChooser.getColorCount();
        System.out.println( "Set selectedColorIndex to " + selectedColorIndex );
    }

    /**
     * Toggles the grid on/off
     */
    protected void toggleGrid() {
        drawGrid = !drawGrid;
    }
}
