package pp;

import processing.core.PApplet;

import java.util.ArrayList;

/**
 * Created by Marcel on 12.01.14.
 */
public class ColorChooser {

    private ArrayList< Integer > colors;
    public ColorChooser( PApplet parent ) {
        colors = new ArrayList<Integer>();

        colors.add( parent.color( 0, 0, 0 ) ); // black
        colors.add( parent.color( 255, 255, 255 ) ); // white
        colors.add( parent.color( 247, 255, 144 ) ); // light yellow
        colors.add( parent.color( 17, 39, 82 ) ); // dark blue
    }

    public int getColor( int index ) {
        return colors.get( index );
    }

    public int getColorCount() {
        return colors.size();
    }
}
