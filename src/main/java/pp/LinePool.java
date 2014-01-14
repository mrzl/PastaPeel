package pp;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.pdf.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Marcel on 12.01.14.
 */
public class LinePool {
    private PApplet parent;
    private PGraphics buffer;

    // holds the mouse position of the last click
    private PVector lastClick;
    private boolean firstClick;

    private ColorChooser colorChooser;
    // the container holding all previously drawn lines
    ConcurrentHashMap< Line, Integer > lines;

    /**
     * LinePool constructor.
     */
    public LinePool( PApplet parent, PGraphics buffer ) {
        this.buffer = buffer;
        this.parent = parent;

        this.colorChooser = new ColorChooser( parent );

        this.firstClick = true;

        this.lines = new ConcurrentHashMap<>();
    }

    /**
     * Adds a line to the container.
     *
     * @param l the line which should be added
     * @param lineColor the color of the added line
     */
    public void addLine( Line l, int lineColor ) {
        lines.put(l, lineColor);
    }

    /**
     * draws all previously laved lines.

     * @param grid the grid, chich indicates the width of a line
     */
    public void drawLines( Grid grid ) {
        int lineWidth = ( int )( grid.getCellWidth() );
        Iterator it = lines.entrySet().iterator();
        buffer.beginDraw();
        while( it.hasNext() ) {
            Map.Entry pairs = ( Map.Entry )it.next();

            int lineColor = (int ) ( pairs.getValue() );
            Line l = ( Line ) ( pairs.getKey() );

            l.draw( lineColor, lineWidth, buffer );
        }
        buffer.endDraw();
    }

    /**
     * every time the mouse is pressed another start-/endpoint of lines is stored.

     * @param grid the Grid, which contains information about its size etc.
     * @param lineColor the currently selected line color
     */
    public void mousePressed( Grid grid, int lineColor, PVector mousePos ) {
        // calculating the X and Y coordinates of the lines snapped to the grid
        int xIndex = ( int ) ( grid.getActiveIndizesXY( mousePos.x, mousePos.y ).x );
        int yIndex = ( int ) ( grid.getActiveIndizesXY( mousePos.x, mousePos.y ).y) ;
        int cellWidth = ( int ) ( grid.getCellWidth() );
        int cellHeight = ( int ) ( grid.getCellHeight() );
        int snappedX = xIndex * cellWidth + ( cellWidth / 2 );
        int snappedY = yIndex * cellHeight + ( cellHeight / 2 );

        // always ignoring the first click of a line
        if( !firstClick ) {
            this.addLine( new Line( ( int ) ( lastClick.x ), ( int ) ( lastClick.y ), snappedX, snappedY ), lineColor );
        }

        // saving the current snapped X for the next click
        lastClick = new PVector( snappedX, snappedY );

        // loop this
        firstClick = !firstClick;
    }

    /**
     * Deletes all saved lines.
     */
    public void clear() {
        lines.clear();
    }

    /**
     * Saves all currently created lines to a file.
     *
     * @param fileName the name of the file
     */
    public void saveToFile( String fileName ) {
        BufferedWriter linePoolWriter = null;
        try {
            File file = new File( fileName );

            linePoolWriter = new BufferedWriter( new FileWriter( file ) );
            int counter = 0;
            // the delimiter, by which all tokens are separated
            String delimiter = ";";

            Iterator it = lines.entrySet().iterator();
            while( it.hasNext() ) {
                Map.Entry pairs = ( Map.Entry )it.next();

                int lineColor = (int ) ( pairs.getValue() );
                Line l = ( Line ) ( pairs.getKey() );

                String lineToWrite = l.getStart().x + delimiter + l.getStart().y + delimiter +
                        l.getEnd().x + delimiter + l.getEnd().y + delimiter +
                        colorChooser.getIndexByColor( lineColor ) + System.getProperty("line.separator");
                linePoolWriter.write(lineToWrite);
                counter++;
            }

        } catch( Exception e ) {
            System.err.println( "Couldn't save file. Abort." );
        } finally {
            try {
              linePoolWriter.close();
            } catch ( NullPointerException e ){
                System.err.println( "Couldn't close the BufferedWriter. Aborting." );
            } catch( IOException e ) {
                System.err.println( "Couldn't close the BufferedWriter. Aborting." );
            }
        }
    }

    /**
     * Loads lines from a file.
     *
     * @param fileName the filename where the lines are saved
     */
    public void loadFromFile( String fileName ) {
        BufferedReader fileReader = null;
        String delimiter = ";";
        String currentLine;
        try {
            fileReader = new BufferedReader( new FileReader( fileName ) );
            while( ( currentLine = fileReader.readLine() ) != null ) {
                String[] fields = currentLine.split( delimiter );
                PVector start = new PVector( Float.parseFloat( fields[ 0 ] ), Float.parseFloat( fields[ 1 ] ) );
                PVector end = new PVector( Float.parseFloat( fields[ 2 ] ), Float.parseFloat( fields[ 3 ] ) );
                int colorIndex = Integer.parseInt( fields[ 4 ] );
                Line loadedLine = new Line( start.x, start.y, end.x, end.y );
                lines.put( loadedLine, colorChooser.getColor( colorIndex ) );
            }
        } catch( Exception e ) {
            System.err.println( "Couldn't load file. Aborting." );
        } finally {
            if( fileReader != null ){
                try {
                    fileReader.close();
                } catch ( Exception e ) {
                    System.err.println( "Couldn't close BufferedReader. Aborting." );
                }
            }
        }
    }

    public void saveLinesToPDF( String fileName, Grid grid ) {
        PGraphics pdf = parent.createGraphics(parent.width, parent.height, PConstants.PDF, fileName );
        pdf.beginDraw();

        Iterator it = lines.entrySet().iterator();
        while( it.hasNext() ) {
            Map.Entry pairs = ( Map.Entry )it.next();

            int lineColor = (int ) ( pairs.getValue() );
            Line l = ( Line ) ( pairs.getKey() );

            l.draw( lineColor, ( int )( grid.getCellWidth() ), pdf );
        }

        pdf.endDraw();
    }
 }
