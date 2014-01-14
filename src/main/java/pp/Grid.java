package pp;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 * Created by Marcel on 12.01.14.
 */
public class Grid {
    private PApplet parent;
    private PGraphics buffer;
    private int cellCountX, cellCountY;

    /**
     * Grid constructor.
     *
     * @param parent
     * @param cellCountX the amount of cells in X direction
     * @param cellCountY the amount of cells in Y direction
     */
    public Grid( PApplet parent, PGraphics buffer, int cellCountX, int cellCountY) {
        this.parent = parent;
        this.buffer = buffer;
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
    }

    /**
     * draws the grid
     */
    public void draw() {
        buffer.beginDraw();
        buffer.pushStyle();
        buffer.noFill();
        buffer.stroke( 255 );
        for( int y = 0; y < cellCountY; y++ ) {
            for( int x = 0; x < cellCountX; x++ ) {
                int xPos = x * buffer.width / cellCountX;
                int yPos = y * buffer.height / cellCountY;
                int boxWidth = buffer.width / cellCountX;
                int boxHeight = buffer.height / cellCountY;
                buffer.rect(xPos, yPos, boxWidth, boxHeight);
            }
        }
        buffer.popStyle();
        buffer.endDraw();
    }

    /**
     * calculates the index of cells from the current mouse Position
     *
     * @return PVector the snapped X and Y indizes of the current mouse position
     */
    PVector getActiveIndizesXY( float mouseX, float mouseY ) {
        int activeIndexX = PApplet.floor( PApplet.map( mouseX, 0, parent.width, 0, cellCountX ) );
        int activeIndexY = PApplet.floor( PApplet.map( mouseY, 0, parent.height, 0, cellCountY ) );
        return new PVector( activeIndexX, activeIndexY );

    }

    /**
     * draws the currently active box
     */
    void drawActiveBox( PVector mousePos ) {
        PVector activeIndizes = getActiveIndizesXY( mousePos.x, mousePos.y );
        buffer.beginDraw();
        buffer.pushStyle();
        buffer.noStroke();
        buffer.fill( 255, 0, 0, 40 );
        buffer.rect(  activeIndizes.x * getCellWidth(), activeIndizes.y * getCellHeight(), getCellWidth(), getCellHeight() );
        buffer.popStyle();
        buffer.endDraw();
    }

    /**
     * returns the width of each cells
     *
     * @return float the width of each cell
     */
    float getCellWidth() {
        return parent.width / cellCountX;
    }

    /**
     * returns the height of each cell
     * 
    * @return float the height of each cell
     */
    float getCellHeight() {
        return parent.height / cellCountY;
    }
}
