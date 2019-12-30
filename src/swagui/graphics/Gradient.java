package swagui.graphics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Colour gradient.
 * @author Alec Dorrington
 */
public class Gradient {
    
    /** The colours in the gradient, anti-clockwise from the bottom-left. */
    private List<Colour> colours;
    
    /**
     * Create a new colour gradient. Requires a subsequent call to setCorners().
     */
    protected Gradient() {}
    
    /**
     * Create a new colour gradient.
     * @params colours in the gradient, anti-clockwise from bottom-left.
     */
    protected Gradient(Colour... colours) { setCorners(colours); }
    
    /**
     * Create a new horizontal colour gradient.
     * @param left colour of left edge of gradient.
     * @param right colour of right edge of gradient.
     * @return the colour gradient.
     */
    public static Gradient horizontal(Colour left, Colour right) {
        return new Gradient(left, left, right, right);
    }
    
    /**
     * Create a new vertical colour gradient.
     * @param top colour of top edge of gradient.
     * @param bottom colour of bottom edge of gradient.
     * @return the colour gradient.
     */
    public static Gradient vertical(Colour top, Colour bottom) {
        return new Gradient(bottom, top, top, bottom);
    }
    
    /**
     * Create a new diagonal colour gradient.
     * @param topRight colour of top-right corner of gradient.
     * @param bottomLeft colour of bottom-left corner of gradient.
     * @return the colour gradient.
     */
    public static Gradient positiveDiagonal(Colour topRight, Colour bottomLeft) {
        Colour mix = (Colour)topRight.mix(bottomLeft, 50);
        return new Gradient(mix, topRight, mix, bottomLeft);
    }
    
    /**
     * Create a new diagonal colour gradient.
     * @param bottomRight colour of bottom-right corner of gradient.
     * @param topLeft colour of top-left corner of gradient.
     * @return the colour gradient.
     */
    public static Gradient negativeDiagonal(Colour bottomRight, Colour topLeft) {
        Colour mix = (Colour)bottomRight.mix(topLeft, 50);
        return new Gradient(bottomRight, mix, topLeft, mix);
    }
    
    /**
    * Creates a lighter version of a gradient.
    * @param amount the amount to lighten the gradient (0-255).
    * @return a lighter version of this gradient.
    */
   public Gradient lighten(int amount) {
       
       return new Gradient(
               (Colour)getCorners().get(0).lighten(amount),
               (Colour)getCorners().get(1).lighten(amount),
               (Colour)getCorners().get(2).lighten(amount),
               (Colour)getCorners().get(3).lighten(amount));
   }
   
   /**
    * Creates a darker version of a gradient.
    * @param amount the amount to darken the gradient (0-255).
    * @return a darker version of this gradient.
    */
   public Gradient darken(int amount) {
       
       return new Gradient(
               (Colour)getCorners().get(0).darken(amount),
               (Colour)getCorners().get(1).darken(amount),
               (Colour)getCorners().get(2).darken(amount),
               (Colour)getCorners().get(3).darken(amount));
   }
    
    /**
     * Mixes this gradient with another.
     * @param gradient the gradient to mix with.
     * @param amount how much of this gradient to add (0-100%).
     * @return the new, mixed gradient.
     */
    public Gradient mix(Gradient gradient, int amount) {
        
        return new Gradient(
               (Colour)getCorners().get(0).mix(gradient.getCorners().get(0), amount),
               (Colour)getCorners().get(1).mix(gradient.getCorners().get(1), amount),
               (Colour)getCorners().get(2).mix(gradient.getCorners().get(2), amount),
               (Colour)getCorners().get(3).mix(gradient.getCorners().get(3), amount));
    }
    
    /**
     * Get the colours in the colour gradient.
     * @return colours in the gradient, anti-clockwise from the bottom-left.
     */
    public List<Colour> getCorners() {
        return colours;
    }
    
    /**
     * Set the colours in the colour gradient.
     * @param colours in the gradient, anti-clockwise from the bottom-left.
     */
    protected void setCorners(Colour... colours) {
        this.colours = Collections.unmodifiableList(Arrays.asList(colours));
    }
}