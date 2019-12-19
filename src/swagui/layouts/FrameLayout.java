package swagui.layouts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * A frame into which other tiles may be placed.
 * @author Alec Dorrington
 */
public class FrameLayout extends Tile {
    
    /** Child tiles in this layout. */
    private List<Tile> children = new LinkedList<>();
    
    /** Size of the border around the frame. */
    private int padding = 0;
    
    /**
     * Create a new frame layout
     * @param scene in which the frame exists.
     * @param children the contents of the frame.
     */
    public FrameLayout(Scene2D scene, Tile... children) {
        super(scene);
        for(Tile tile : children) this.children.add(tile);
        setFill(Fill.FILL_PARENT, Fill.FILL_PARENT);
        setVisible(false);
        setDepth(2);
        update();
    }
    
    /**
     * Add a new tile to end of the list.
     * @param tile to add.
     * @return this list.
     */
    public FrameLayout addTile(Tile tile) {
        children.add(tile);
        update();
        return this;
    }
    
    /**
     * Remove a tile from the list.
     * @param tile to remove.
     * @return this list.
     */
    public FrameLayout removeTile(Tile tile) {
        children.remove(tile);
        update();
        return this;
    }
    
    /**
     * @return the contents of this list.
     */
    public List<Tile> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * @return the size of the border around the frame (pixels).
     */
    public int getPadding() { return padding; }
    
    /**
     * @param padding size of border around the frame (pixels).
     * @return this list.
     */
    public FrameLayout setPadding(int padding) {
        this.padding = padding;
        update();
        return this;
    }
    
    @Override
    public void update() {
        updateWidth();
        updateHeight();
        updateX();
        updateY();
    }
    
    /**
     * Update widths of this frame and its children.
     */
    private void updateWidth() {
        
        //If set to match size of contents horizontally.
        if(getHFill() == Fill.WRAP_CONTENT) {
            
            //Smallest x-value inside layout.
            int minX = children.stream()
                .mapToInt(Tile::getMinX)
                .min().getAsInt();
            
            //Largest x-value inside layout.
            int maxX = children.stream()
                .mapToInt(Tile::getMaxX)
                .max().getAsInt();
            
            setWidth(maxX - minX + 2*getPadding());
            
        } else {
            
            //Set relative-width tiles to match width of frame.
            children.stream()
                .filter(t -> t.getHFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setWidth(getWidth() - 2*getPadding()));
        }
    }
    
    /**
     * Update heights of this frame and its children.
     */
    private void updateHeight() {
        
        //If set to match size of contents vertically.
        if(getVFill() == Fill.WRAP_CONTENT) {
            
            //Smallest y-value inside layout.
            int minY = children.stream()
                .mapToInt(Tile::getMinY)
                .min().getAsInt();
            
            //Largest y-value inside layout.
            int maxY = children.stream()
                .mapToInt(Tile::getMaxY)
                .max().getAsInt();
            
            setHeight(maxY - minY + 2*getPadding());
            
        } else {
            
            //Set relative-height tiles to match height of frame.
            children.stream()
                .filter(t -> t.getVFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setHeight(getHeight() - 2*getPadding()));
        }
    }
    
    /**
     * Update x-coordinate of this frame's children.
     */
    private void updateX() {
        
        getChildren().forEach(t -> {
            switch(t.getAlignment()) {
            
            //Tile aligns with left edge of list.
            case BOTTOM_LEFT: case LEFT: case TOP_LEFT:
                t.setX(getX() - getWidth()/2 + getPadding() + t.getWidth()/2);
                break;
            
            //Tile aligns with center of list.
            case BOTTOM: case CENTER: case TOP:
                t.setX(getX());
                break;
            
            //Tile aligns with right edge of list.
            case BOTTOM_RIGHT: case RIGHT: case TOP_RIGHT:
                t.setX(getX() + getWidth()/2 - getPadding() - t.getWidth()/2);
                break;
            }
        });
    }
    
    /**
     * Update y-coordinate of this frame's children.
     */
    private void updateY() {
        
        getChildren().forEach(t -> {
            switch(t.getAlignment()) {
            
            //Tile aligns with bottom edge of list.
            case BOTTOM_LEFT: case BOTTOM: case BOTTOM_RIGHT:
                t.setY(getY() - getHeight()/2 + getPadding() + t.getHeight()/2);
                break;
            
            //Tile aligns with center of list.
            case LEFT: case CENTER: case RIGHT:
                t.setY(getY());
                break;
            
            //Tile aligns with top edge of list.
            case TOP_LEFT: case TOP: case TOP_RIGHT:
                t.setY(getY() + getHeight()/2 - getPadding() - t.getHeight()/2);
                break;
            }
        });
    }
}