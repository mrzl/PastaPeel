package pp;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.ArrayList;

/**
 * Created by Marcel on 12.01.14.
 */
public class ColorChooser {
    // an arraylist containing all available colors
    private ArrayList< Integer > colors;
    private ArrayList< ColorPalette > palettes;

    /**
     * the ColorChooser Contructor
     *
     * @param PApplet
     */
    public ColorChooser( PApplet parent ) {
        colors = new ArrayList<>();
        palettes = new ArrayList<>();

        // adds lots of colors
        colors.add( parent.color( 0, 0, 0 ) ); // black
        colors.add( parent.color( 255, 255, 255 ) ); // white
        colors.add( parent.color( 247, 255, 144 ) ); // light yellow
        colors.add( parent.color( 17, 39, 82 ) ); // dark blue
    }

    /**
     * returns a color from the palette
     *
     * @param index the index of the color from the current palette
     *
     * @return int the color, which is stored at the passed index
     */
    public int getColor( int index ) {
        return colors.get( index );
    }

    /**
     * returns the amount of saved colors.
     *
     * @return int the currently available number of colors
     */
    public int getColorCount() {
        return colors.size();
    }

    /**
     * returns the index of a color
     *
     * @param color the color, of which this function returns the index from the palette
     *
     * @return int the index of the passed color
     */
    public int getIndexByColor( int color ) {
        int index = 0;
        // loop through all saved colors
        for( int savedColor : colors ) {
            // if they are equal
            if( savedColor == color ) {
                // return the current index
                return index;
            }
            // increase the index with every loop
            index++;
        }
        // if no equal color was founds, 0 is returned.
        return index;
    }
}
