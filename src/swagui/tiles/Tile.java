package swagui.tiles;

import java.util.Optional;

import swagui.graphics.Colour;
import swagui.graphics.Gradient;
import swagui.graphics.Texture;
import swagui.layouts.Layout.Align;
import swagui.layouts.Layout.Fill;
import swagui.math.Matrix4;

/**
 * Rectangular window element.
 * @author Alec Dorrington
 */
public class Tile {
    
    /** The tile alignment for determining position within a view. */
    private Align alignment = Align.CENTER;
    
    /** The position of the tile (pixels). */
    private int x, y;
    
    /** The fill mode for determining tile size. */
    private Fill fill = Fill.ABSOLUTE;
    
    /** The size of the tile (pixels). */
    private int width = 100, height = 100;
    
    /** The relative size of the tile. */
    private int hWeight = 1, vWeight = 1;
    
    /** The aspect ratio of the tile (width:height). */
    private float aspectRatio = 1.0F;
    
    /** The angle of the tile (degrees, anti-clockwise). */
    private int angle = 0;
    
    /** The depth of the tile (ordering, 0-99, default=50). */
    private int depth = 50;
    
    /** The colour of the tile */
    private Gradient colour = Colour.SEARBROOK;
    
    /** The texture of the tile. */
    private Texture texture;
    
    /** Whether the tile is to be rendered. */
    private boolean visible = true;
    
    /**
     * Create a new tile.
     */
    public Tile() {}
    
    /**
     * Create a new tile.
     * @param scene in which the tile exists.
     * @param x x-coordinate of the tile (pixels).
     * @param y y-coordinate of the tile (pixels).
     * @param width of the tile (pixels, left-to-right).
     * @param height of the tile (pixels, bottom-to-top).
     */
    public Tile(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);
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
    
    /** @return the tile alignment for positioning within a view. */
    public Align getAlignment() { return alignment; }
    
    /**
     * Set the tile alignment for positioning within a view.
     * @param alignment of tile.
     * @return this tile.
     */
    public Tile setAlignment(Align alignment) {
        this.alignment = alignment;
        return this;
    }
    
    /** @return x-coordinate of the tile (pixels, left-to-right). */
    public int getX() { return x; }
    
    /** @return y-coordinate of the tile (pixels, bottom-to-top). */
    public int getY() { return y; }
    
    /**
     * @param x coordinate of the tile (pixels, left-to-right).
     * @return this tile.
     */
    public Tile setX(int x) {
        this.x = x;
        return this;
    }
    
    /**
     * @param y coordinate of the tile (pixels, bottom-to-top).
     * @return this tile.
     */
    public Tile setY(int y) {
        this.y = y;
        return this;
    }
    
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
    
    /** @return tile fill mode for determining size. */
    public Fill getFill() { return fill; }
    
    /**
     * Set the tile fill mode for determining size.
     * @param fill tile fill mode.
     * @return this tile.
     */
    public Tile setFill(Fill fill) {
        this.fill = fill;
        return this;
    }
    
    /** @return width of the tile (pixels). */
    public int getWidth() { return width; }
    
    /** @return height of the tile (pixels). */
    public int getHeight() { return height; }
    
    /**
     * @param width of the tile (pixels).
     * @return this tile.
     */
    public Tile setWidth(int width) {
        this.width = width;
        return this;
    }
    
    /**
     * @param height of the tile (pixels).
     * @return this tile.
     */
    public Tile setHeight(int height) {
        this.height = height;
        return this;
    }
    
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
    
    /** @return relative horizontal weight. */
    public int getHWeight() { return hWeight; }
    
    /** @return relative vertical weight. */
    public int getVWeight() { return vWeight; }
    
    /**
     * @param hWeight relative horizontal weight.
     * @return this tile.
     */
    public Tile setHWeight(int hWeight) {
        this.hWeight = hWeight;
        return this;
    }
    
    /**
     * @param vWeight relative vertical weight.
     * @return this tile.
     */
    public Tile setVWeight(int vWeight) {
        this.vWeight = vWeight;
        return this;
    }
    
    /**
     * Set tile weights for relative size.
     * @param hWeight relative horizontal weight.
     * @param vWeight relative vertical weight.
     * @return this tile.
     */
    public Tile setWeights(int hWeight, int vWeight) {
        this.hWeight = hWeight;
        this.vWeight = vWeight;
        return this;
    }
    
    /** @return the aspect ratio of the tile (width:height). */
    public float getAspectRatio() { return aspectRatio; }
    
    /**
     * Set the aspect ratio of the tile.
     * @param apsectRatio of the tile (width:height).
     * @return this tile.
     */
    public Tile setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        return this;
    }
    
    /** @return angle of the tile (degrees, anti-clockwise). */
    public int getAngle() { return angle; }
    
    /**
     * Set the orientation of the tile.<br>
     * Note: this currently doesn't work with buttons or layouts.
     * @param angle of the tile (degrees, anti-clockwise).
     * @return this tile.
     */
    public Tile setAngle(int angle) {
        this.angle = angle;
        return this;
    }
    
    /** @return depth of the tile (ordering, 0-99, default=50). */
    public int getDepth() { return depth; }
    
    /**
     * Set the depth of the tile (ordering).
     * @param depth of the tile (0-99, default=50).
     * @return this tile.
     */
    public Tile setDepth(int depth) {
        this.depth = depth;
        return this;
    }
    
    /** @return colour of the tile. */
    public Gradient getColour() { return colour; }
    
    /**
     * Set the colour of the tile.
     * @param colour of the tile.
     * @return this tile.
     */
    public Tile setColour(Gradient colour) {
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
    
    /** @return whether the tile is visible to the renderer. */
    public boolean isVisible() { return visible; }
    
    /**
     * Set whether the tile is visible to the renderer.
     * @param visible whether the tile is to be rendered.
     * @return this tile.
     */
    public Tile setVisible(boolean visible) {
        this.visible = visible;
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
    
    /** Update the position/size of this tile and its children. */
    public void update() {}
}