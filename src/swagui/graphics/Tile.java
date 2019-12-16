package swagui.graphics;

import java.util.Optional;

/**
 * Rectangular window element.
 * @author Alec Dorrington
 */
public class Tile {
    
    /**
     * Create a new tile.
     * @param x x-coordinate of the tile (pixels).
     * @param y y-coordinate of the tile (pixels).
     * @param width of the tile (pixels, left-to-right).
     * @param height of the tile (pixels, bottom-to-top).
     */
    public Tile(int x, int y, int width, int height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        TileShader.INSTANCE.addTile(this);
    }
    
    /**
     * Calculates the transformation matrix for this tile.
     * @return the 4x4 transformation matrix.
     */
    public Matrix4 getTransform() {
        
        //Create scale, rotation and translation matrices.
        Matrix4 scale = Matrix4.getScaleMatrix(width, height, 1.0F);
        Matrix4 rotation = Matrix4.getRotationMatrix(angle);
        Matrix4 translation = Matrix4.getTranslationMatrix(
                x, y, 1.0F-(depth+1)/100.0F);
        
        //Transformation is product of matrices.
        return translation.mul(rotation.mul(scale));
    }
    
    /**
     * Remove this tile from the window.
     */
    public void delete() {
        TileShader.INSTANCE.removeTile(this);
    }
    
    /** The x-coordinate of the tile (pixels). */
    private int x;
    
    /** @return x-coordinate of the tile (pixels). */
    public int getX() { return x; }
    
    /** @param x x-coordinate of the tile (pixels). */
    public void setX(int x) {
        this.x = x;
    }
    
    /** The y-coordinate of the tile (pixels). */
    private int y;
    
    /** @return y-coordinate of the tile (pixels). */
    public int getY() { return y; }
    
    /** @param y y-coordinate of the tile (pixels). */
    public void setY(int y) {
        this.y = y;
    }
    
    /** The width of the tile (pixels, left-to-right). */
    private int width = 100;
    
    /** @return width of the tile (pixels, left-to-right). */
    public int getWidth() { return width; }
    
    /** @param width of the tile (pixels, left-to-right). */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /** The height of the tile (pixels, bottom-to-top). */
    private int height = 100;
    
    /** @return height of the tile (pixels, bottom-to-top). */
    public int getHeight() { return height; }
    
    /** @param height of the tile (pixels, bottom-to-top). */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /** The angle of the tile (degrees, anti-clockwise). */
    private int angle = 0;
    
    /** @return angle of the tile (degrees, anti-clockwise). */
    public int getAngle() { return angle; }
    
    /** @param angle of the tile (degrees, anti-clockwise). */
    public void setAngle(int angle) {
        this.angle = angle;
    }
    
    /** The depth of the tile (ordering, 0-99). */
    private int depth = 0;
    
    /** @return depth of the tile (ordering, 0-99). */
    public int getDepth() { return depth; }
    
    /** @param depth of the tile (ordering, 0-99). */
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    /** The colour of the tile */
    private Colour colour = Colour.WHITE;
    
    /** @return colour of the tile. */
    public Colour getColour() { return colour; }
    
    /** @param colour of the tile. */
    public void setColour(Colour colour) {
        this.colour = colour;
    }
    
    /** The texture of the tile. */
    private Texture texture ;
    
    /** @return texture of the tile. */
    public Optional<Texture> getTexture() {
        return Optional.ofNullable(texture);
    }
    
    /** @param texture of the tile. */
    public void setTexture(Texture texture) { this.texture = texture; }
    
    /** @return x-coordinate of left edge of tile. */
    public int getMinX() {
        return x - width/2;
    }
    
    /** @return x-coordinate of right edge of tile. */
    public int getMaxX() {
        return x + width/2;
    }
    
    /** @return y-coordinate of bottom edge of tile. */
    public int getMinY() {
        return y - height/2;
    }
    
    /** @return y-coordinate of top edge of tile. */
    public int getMaxY() {
        return y + height/2;
    }
}