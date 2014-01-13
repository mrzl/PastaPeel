package pp;

import processing.core.PApplet;

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
    // bool in order to save a single frame from the animation
    private boolean savePdf;
    // filename of the exported PDF
    private String currentFileNamePDF;
    // filename of the exported PNG
    private String currentFileNamePNG;

    private ControlFrame controlFrame;
    private boolean drawGrid;

    @Override
    public void setup() {
        size(800, 800, P2D);
        grid = new Grid( this, 50, 50 );
        linePool = new LinePool( this );
        colorChooser = new ColorChooser( this );
        selectedColorIndex = 0;
        drawGrid = true;

        controlFrame = ControlFrame.createControlFrame( this, "Controls", 400, 600 );
    }

    @Override
    public void draw() {
        if( savePdf ) {
            beginRecord( PDF, currentFileNamePDF);
        }
        background( 127 );

        if( !savePdf ) {
            if( drawGrid ) {
                grid.draw();
            }
            grid.drawActiveBox();
        }

        linePool.drawLines( ( int )( grid.getCellWidth() ) );
        if( savePdf ) {
            endRecord();
            savePdf = false;
        }
    }

    @Override
    public void mousePressed() {
        linePool.mousePressed( grid, colorChooser.getColor( selectedColorIndex ) );
    }

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
        }
    }

    protected void saveConfiguration( String lastLinesFileName ) {
        linePool.saveToFile( lastLinesFileName );
        System.out.println( "Saved lines to " +  lastLinesFileName);
    }


     protected void loadConfiguration( String loadLinesFileName ) {
        linePool.loadFromFile( loadLinesFileName );
        System.out.println( "Loaded lines from " + loadLinesFileName );
    }

    protected void clearLines() {
        linePool.clear();
    }
    /*
    Saves a PDF with the current output.
     */
     private void savePDF() {
         savePdf = true;
         currentFileNamePDF = "pasta_peel-" + getTimeStamp() + ".pdf";
    }

    /*
    Saves a PNG with the current output.
     */
    private void savePNG() {
        currentFileNamePNG = "pasta_peel-" + getTimeStamp() + ".png";
        saveFrame(currentFileNamePNG);
    }

    /*
    creates a String encoding the current timestamp
    @return String the String containing the current timestamp
     */
    public String getTimeStamp() {
        Calendar now = Calendar.getInstance();
        return String.format( "%1$ty%1$tm%1$td_%1$tH%1$tM%1$tS", now );
    }

    /*
    The main function. starting this PApplet.
     */
    public static void main( String[] args ) {
        // starting this PApplet
        PApplet.main( new String[ ] { "pp.PastaPeelPApplet" } );
    }

    public void drawCurrentColor( PApplet controlWindow ) {
        controlWindow.stroke( 255 );
        controlWindow.fill( colorChooser.getColor( selectedColorIndex ) );
        controlWindow.rect( 140, 30, 50, 50 );
    }

    protected void randomColor( ) {
        selectedColorIndex++;
        selectedColorIndex %= colorChooser.getColorCount();
        System.out.println( "Set selectedColorIndex to " + selectedColorIndex );
    }

    protected void toggleGrid() {
        drawGrid = !drawGrid;
    }
}
