package pp;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Created by Marcel on 12.01.14.
 */
public class Grid {

    private PApplet parent;
    private int cellCountX, cellCountY;

    /*
    Grid constructor.
    @param parent in order to let this class draw on the main context
    @param cellCountX the amount of cells in X direction
    @param cellCountY the amount of cells in Y direction
     */
    public Grid( PApplet parent, int cellCountX, int cellCountY) {
        this.parent = parent;
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
    }

    /*
    draws the grid
     */
    public void draw() {
        parent.pushStyle();
        parent.noFill();
        parent.stroke( 255 );
        for( int y = 0; y < cellCountY; y++ ) {
            for( int x = 0; x < cellCountX; x++ ) {
                int xPos = x * parent.width / cellCountX;
                int yPos = y * parent.height / cellCountY;
                int boxWidth = parent.width / cellCountX;
                int boxHeight = parent.height / cellCountY;
                parent.rect( xPos, yPos, boxWidth, boxHeight );
            }
        }
        parent.popStyle();
    }

    /*
    calculates the index of cells from the current mouse Position
    @return PVector the snapped X and Y indizes of the current mouse position
     */
    PVector getActiveIndizesXY() {
        int activeIndexX = PApplet.floor( PApplet.map( parent.mouseX, 0, parent.width, 0, cellCountX ) );
        int activeIndexY = PApplet.floor( PApplet.map( parent.mouseY, 0, parent.height, 0, cellCountY ) );
        return new PVector( activeIndexX, activeIndexY );

    }

    /*
    draws the currently active box
     */
    void drawActiveBox() {
        PVector activeIndizes = getActiveIndizesXY();
        parent.pushStyle();
        parent.noStroke();
        parent.fill( 255, 0, 0, 40 );
        parent.rect(  activeIndizes.x * getCellWidth(), activeIndizes.y * getCellHeight(), getCellWidth(), getCellHeight() );
        parent.popStyle();
    }

    /*
    returns the width of each cells
    @return float the width of each cell
     */
    float getCellWidth() {
        return parent.width / cellCountX;
    }

    /*
    returns the height of each cell
    @return float the height of each cell
     */
    float getCellHeight() {
        return parent.height / cellCountY;
    }
}
