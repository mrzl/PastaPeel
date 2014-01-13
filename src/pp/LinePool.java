package pp;

import processing.core.PApplet;
import processing.core.PVector;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Marcel on 12.01.14.
 */
public class LinePool {
    private PApplet parent;

    // holds the mouse position of the last click
    private PVector lastClick;
    // the container holding all previously drawn lines
    private ArrayList< Line > lineContainer;
    // container holding the specific color for each line. TODO: java.util.HashMap should do it, too.
    private ArrayList< Integer > colorContainer;
    // class that holts all different tape colors
    private ColorChooser colorChooser;

    /*
    LinePool constructor.
     */
    public LinePool( PApplet parent ) {
        this.parent = parent;

        colorChooser = new ColorChooser( this.parent );

        lineContainer = new ArrayList<Line>();
        colorContainer = new ArrayList<Integer>();
    }

    /*
    Adds a line to the container.
     */
    public void addLine( Line l ) {
        lineContainer.add( l );
        // adds a random color for now.
        colorContainer.add( colorChooser.getColor( ( int ) ( parent.random( colorChooser.getColorCount() ) ) ) );
    }

    /*
    draws all previously laved lines.
    @param lineWidth the width of the line
     */
    public void drawLines( int lineWidth ) {
        int index = 0;
        for( Line l : lineContainer ){
            int lineColor = colorContainer.get( index );
            l.draw( lineColor, lineWidth );
            index++;
        }
    }

    /*
    everytime the mouse is pressed another start-/endpoint of lines is stored.
     */
    public void mousePressed( Grid grid ) {
        // calculating the X and Y coordinates of the lines snapped to the grid
        int xIndex = ( int ) ( grid.getActiveIndizesXY().x );
        int yIndex = ( int ) ( grid.getActiveIndizesXY().y) ;
        int cellWidth = ( int ) ( grid.getCellWidth() );
        int cellHeight = ( int ) ( grid.getCellHeight() );
        int snappedX = xIndex * cellWidth + ( cellWidth / 2 );
        int snappedY = yIndex * cellHeight + ( cellHeight / 2 );

        // ignoring the very first click, because there is no firstClick
        if( lastClick != null ) {
            this.addLine( new Line( parent, ( int ) ( lastClick.x ), ( int ) ( lastClick.y ), snappedX, snappedY ) );
        }

        // saving the current snapped X for the next click
        lastClick = new PVector( snappedX, snappedY );
    }

    /*
    Saves all currently created lines to a file.
    @param String fileName the name of the file
     */
    public void saveToFile( String fileName ) {
        BufferedWriter linePoolWriter = null;
        try {
            File file = new File( fileName );

            linePoolWriter = new BufferedWriter( new FileWriter( file ) );
            int counter = 0;
            // the delimiter, by which all tokens are separated
            String delimiter = ";";
            for( Line l : lineContainer ) {
                String lineToWrite = l.getStart().x + delimiter + l.getStart().y + delimiter +
                        l.getEnd().x + delimiter + l.getEnd().y + delimiter +
                        counter + System.getProperty("line.separator");
                linePoolWriter.write( lineToWrite );
                counter++;
            }

        } catch( Exception e ) {
            System.err.println( "Couldn't save file. Abort." );
        } finally {
            try {
              linePoolWriter.close();
            } catch ( Exception e ){
                System.err.println( "Couldn't close the BufferedWriter. Aborting." );
            }
        }
    }

    /*
    Loads lines from a file.
    @param String fileName the filename where the lines are saved
     */
    public void loadFromFile( String fileName ) {
        BufferedReader fileReader = null;
        String delimiter = ";";
        String currentLine = "";
        try {
            fileReader = new BufferedReader( new FileReader( fileName ) );
            while( ( currentLine = fileReader.readLine() ) != null ) {
                String[] fields = currentLine.split( delimiter );
                PVector start = new PVector( Float.parseFloat( fields[ 0 ] ), Float.parseFloat( fields[ 1 ] ) );
                PVector end = new PVector( Float.parseFloat( fields[ 2 ] ), Float.parseFloat( fields[ 3 ] ) );
                int colorIndex = Integer.parseInt( fields[ 4 ] );
                Line loadedLine = new Line( parent, start.x, start.y, end.x, end.y );
                lineContainer.add( loadedLine );
                colorContainer.add( colorChooser.getColor( colorIndex ) );
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
 }
