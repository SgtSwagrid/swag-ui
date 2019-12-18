package swagui.graphics;

/**
 * RGBA colour.
 * @author Alec Dorrington
 */
public class Colour {
    
    /** White: 0xFFFFFF. */
    public static final Colour WHITE = hex(0xFFFFFF);
    
    /** Black: 0x000000. */
    public static final Colour BLACK = hex(0x000000);
    
    /** Colour channel values (0-255). */
    public final int R, G, B, A;
    
    /**
     * Create a new colour using RGBA (0-255).
     * @param r red channel value (0-255).
     * @param g green channel value (0-255).
     * @param b blue channel value (0-255).
     * @param a alpha (opacity) channel value (0-255).
     */
    private Colour(int r, int g, int b, int a) {
        R = r<0 ? r : r>255 ? 255 : r;
        G = g<0 ? g : g>255 ? 255 : g;
        B = b<0 ? b : b>255 ? 255 : b;
        A = a<0 ? a : a>255 ? 255 : a;
    }
    
    /**
     * Create a new colour using RGB (0-255).
     * @param r red channel value (0-255).
     * @param g green channel value (0-255).
     * @param b blue channel value (0-255).
     * @return the new colour.
     */
    public static Colour rgb(int r, int g, int b) {
        return new Colour(r, g, b, 255);
    }
    
    /**
     * Create a new colour using RGBA (0-255).
     * @param r red channel value (0-255).
     * @param g green channel value (0-255).
     * @param b blue channel value (0-255).
     * @param a alpha (opacity) channel value (0-255).
     * @return the new colour.
     */
    public static Colour rgba(int r, int g, int b, int a) {
        return new Colour(r, g, b, a);
    }
    
    /**
     * Create a new colour using hexadecimal (0x000000-0xFFFFFF).
     * @param hex the colour value in hexadecimal.
     * @return the new colour.
     */
    public static Colour hex(int hex) {
        return new Colour(hex>>16, (hex>>8)&0xFF, hex&0xFF, 255);
    }
    
    /**
     * Creates a lighter version of a colour.
     * @param amount the amount to lighten the colour (0-255).
     * @return a lighter version of this colour.
     */
    public Colour lighten(int amount) {
        return new Colour(R+amount, G+amount, B+amount, A);
    }
    
    /**
     * Creates a darker version of a colour.
     * @param amount the amount to darken the colour (0-255).
     * @return a darker version of this colour.
     */
    public Colour darken(int amount) {
        return new Colour(R-amount, G-amount, B-amount, A);
    }
    
    /**
     * Mixes this colour with another.
     * @param colour the colour to mix with.
     * @param amount how much of this colour to add (0-100%).
     * @return the new, mixed colour.
     */
    public Colour mix(Colour colour, int amount) {
        return new Colour(
                (100-amount)*R/100 + amount*colour.R/100,
                (100-amount)*G/100 + amount*colour.G/100,
                (100-amount)*B/100 + amount*colour.B/100,
                (100-amount)*A/100 + amount*colour.A/100);
    }
}