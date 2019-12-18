package swagui.tiles;

import java.util.Optional;

import swagui.graphics.Colour;
import swagui.graphics.Texture;
import swagui.math.Matrix4;

/**
 * Rectangular window element.
 * @author Alec Dorrington
 */
public class Tile {
    
    /** The scene in which this tile exists. */
    private Scene2D scene;
    
    /** The position of the tile (pixels). */
    private int x, y;
    
    /** The size of the tile (pixels). */
    private int width = 100, height = 100;
    
    /** The angle of the tile (degrees, anti-clockwise). */
    private int angle = 0;
    
    /** The depth of the tile (ordering, 0-99). */
    private int depth = 0;
    
    /** The colour of the tile */
    private Colour colour = Colour.SEARBROOK;
    
    /** The texture of the tile. */
    private Texture texture;
    
    /**
     * Create a new tile.
     * @param scene in which the tile exists.
     */
    public Tile(Scene2D scene) {
        this.scene = scene;
        scene.addTile(this);
    }
    
    /**
     * Create a new tile.
     * @param scene in which the tile exists.
     * @param x x-coordinate of the tile (pixels).
     * @param y y-coordinate of the tile (pixels).
     * @param width of the tile (pixels, left-to-right).
     * @param height of the tile (pixels, bottom-to-top).
     */
    public Tile(Scene2D scene, int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
        this.scene = scene;
        scene.addTile(this);
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
     * Remove this tile from the scene.
     */
    public void delete() {
        scene.removeTile(this);
    }
    
    /** @return x-coordinate of the tile (pixels, left-to-right). */
    public int getX() { return x; }
    
    /** @return y-coordinate of the tile (pixels, bottom-to-top). */
    public int getY() { return y; }
    
    /**
     * Set the position of the tile.
     * @param x coordinate of the tile (pixels, left-to-right).
     * @param y coordinate of the tile (pixels, bottom-to-top).
     * @return this tile.
     */
    public Tile setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    /** @return width of the tile (pixels). */
    public int getWidth() { return width; }
    
    /** @return height of the tile (pixels). */
    public int getHeight() { return height; }
    
    /**
     * Set the size of the tile.
     * @param width of the tile (pixels).
     * @param height of the tile (pixels).
     * @return this tile.
     */
    public Tile setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }
    
    /** @return angle of the tile (degrees, anti-clockwise). */
    public int getAngle() { return angle; }
    
    /**
     * Set the orientation of the tile.
     * @param angle of the tile (degrees, anti-clockwise).
     * @return this tile.
     */
    public Tile setAngle(int angle) {
        this.angle = angle;
        return this;
    }
    
    /** @return depth of the tile (ordering, 0-99). */
    public int getDepth() { return depth; }
    
    /**
     * Set the depth of the tile (ordering).
     * @param depth of the tile (0-99).
     * @return this tile.
     */
    public Tile setDepth(int depth) {
        this.depth = depth;
        return this;
    }
    
    /** @return colour of the tile. */
    public Colour getColour() { return colour; }
    
    /**
     * Set the colour of the tile.
     * @param colour of the tile.
     * @return this tile.
     */
    public Tile setColour(Colour colour) {
        this.colour = colour;
        return this;
    }
    
    /** @return texture of the tile. */
    public Optional<Texture> getTexture() {
        return Optional.ofNullable(texture);
    }
    
    /**
     * Set the texture of the tile.
     * @param texture of the tile (or null).
     * @return this tile.
     */
    public Tile setTexture(Texture texture) {
        this.texture = texture;
        return this;
    }
    
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