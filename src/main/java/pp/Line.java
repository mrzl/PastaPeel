package pp;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by Marcel on 12.01.14.
 */
public class Line {

    private float x1, x2, y1, y2;

    /**
     * Line constructor specifying all coordnates of the line
     */
    public Line( float x1, float y1, float x2, float y2 ) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    /**
     * draws the line
     *
     * @param color the color of the line
     * @param lineWidth the width of the line
     */
    void draw( int color, int lineWidth, PGraphics buffer ) {
        buffer.pushStyle();
        buffer.strokeCap(PConstants.PROJECT);
        buffer.stroke(color);
        buffer.strokeWeight( lineWidth );
        buffer.noFill();
        buffer.line( x1, y1, x2, y2 );
        buffer.popStyle();
    }

    /**
     * returns a vector which contains the coordinates of the start point from this line
     *
     * @return PVector the start point of the line
     */
    public PVector getStart() {
        return new PVector( x1, y1 );
    }

    /**
     * returns a vector which contains the coordinates of the end point from this line
     *
     * @return PVector the end point of the line
     */
    public PVector getEnd() {
        return new PVector( x2, y2 );
    }
}
