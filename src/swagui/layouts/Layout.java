package swagui.layouts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import swagui.tiles.Tile;

/**
 * Superclass for layouts, which automatic tile placement.
 * @author Alec Dorrington
 */
public class Layout extends Tile {
    
    /**
     * Method for determining tile position.
     */
    public enum Align {
        
        //Relative location of the tile.
        TOP_LEFT(0.0F, 1.0F),
        TOP(0.5F, 1.0F),
        TOP_RIGHT(1.0F, 1.0F),
        LEFT(0.0F, 0.5F),
        CENTER(0.5F, 0.5F),
        RIGHT(1.0F, 0.5F),
        BOTTOM_LEFT(0.0F, 0.0F),
        BOTTOM(0.5F, 0.0F),
        BOTTOM_RIGHT(1.0F, 0.0F);
        
        /** Horizontal/vertical alignment mode, 0.0-1.0. */
        public final float H_ALIGNMENT, V_ALIGNMENT;
        
        private Align(float h, float v) {
            H_ALIGNMENT = h; V_ALIGNMENT = v;
        }
    }
    
    /**
     * Method for determining tile size.
     */
    public enum Fill {
        
        /** Tile size is specified in pixels. */
        ABSOLUTE(true, true, false, false, false, false, false),
        /** Tile size is specified relative to parent. */
        FILL_PARENT(false, false, true, true, false, false, false),
        /** Tile size is automatically determined by it's contents. */
        WRAP_CONTENT(false, false, false, false, true, true, false),
        
        /** Tile size is determined by its parent and aspect ratio. */
        FILL_PARENT_ASPECT(false, false, true, true, false, false, true),
        /** Tile size is determined by its contents and aspect ratio. */
        WRAP_CONTENT_ASPECT(false, false, false, false, true, true, true),
        
        //Mixed fill modes.
        H_ABSOLUTE_V_FILL_PARENT(true, false, false, true, false, false, false),
        H_ABSOLUTE_V_WRAP_CONTENT(true, false, false, false, false, true, false),
        H_FILL_PARENT_V_ABSOLUTE(false, true, true, false, false, false, false),
        H_FILL_PARENT_V_WRAP_CONTENT(false, false, true, false, false, true, false),
        H_WRAP_CONTENT_V_ABSOLUTE(false, true, false, false, true, false, false),
        H_WRAP_CONTENT_V_FILL_PARENT(false, false, false, true, true, false, false);
        
        /** Whether this fill mode uses absolute sizing. */
        public final boolean H_ABSOLUTE, V_ABSOLUTE;
        /** Whether this fill mode uses fill sizing. */
        public final boolean H_FILL_PARENT, V_FILL_PARENT;
        /** Whether this fill mode uses wrap sizing. */
        public final boolean H_WRAP_CONTENT, V_WRAP_CONTENT;
        /** Whether this fill mode uses an aspect ratio. */
        public final boolean ASPECT_RATIO;
        
        private Fill(boolean hAbs, boolean vAbs, boolean hFill, boolean vFill,
                boolean hWrap, boolean vWrap, boolean aspect) {
            
            H_ABSOLUTE = hAbs; V_ABSOLUTE = vAbs;
            H_FILL_PARENT = hFill; V_FILL_PARENT = vFill;
            H_WRAP_CONTENT = hWrap; V_WRAP_CONTENT = vWrap;
            ASPECT_RATIO = aspect;
        }
    }
    
    /** Contents of this layout. */
    private List<Tile> children = new LinkedList<>();
    
    /** Size of the border around the layout. */
    private int padding = 0;
    
    /**
     * Create a new layout.
     * @param scene in which the layout exists.
     * @param children the contents of the layout.
     */
    public Layout(Tile... children) {
        super();
        for(Tile tile : children) addTile(tile);
        setFill(Fill.FILL_PARENT);
        setVisible(false);
        setDepth(1);
    }
    
    /**
     * Add a new tile to end of the layout.
     * @param tile to add.
     * @return this layout.
     */
    public Layout addTile(Tile tile) {
        children.add(tile);
        return this;
    }
    
    /**
     * Add a new tile to the layout.
     * @param tile to add.
     * @param index position at which tile is inserted.
     * @return this layout.
     */
    public Layout addTile(Tile tile, int index) {
        children.add(index, tile);
        return this;
    }
    
    /**
     * Remove a tile from the layout.
     * @param tile to remove.
     * @return this layout.
     */
    public Layout removeTile(Tile tile) {
        children.remove(tile);
        return this;
    }
    
    /**
     * Remove all tiles from the layout.
     * @return this layout.
     */
    public Layout clearChildren() {
        children.clear();
        return this;
    }
    
    /**
     * @return the contents of this layout.
     */
    public List<Tile> getChildren() {
        return Collections.unmodifiableList(children);
    }
    
    /**
     * @return all ancestors of this layout.
     */
    public List<Tile> getAncestors() {
        
        List<Tile> ancestors = new LinkedList<>();
        //Include direct children.
        ancestors.addAll(children);
        //Include all ancestors of direct children.
        ancestors.addAll(children.stream()
            .filter(t -> t instanceof Layout)
            .map(l -> (Layout) l)
            .flatMap(l -> l.getAncestors().stream())
            .collect(Collectors.toList()));
        return ancestors;
    }
    
    /**
     * @return the size of the border around the layout (pixels).
     */
    public int getPadding() { return padding; }
    
    /**
     * @param padding size of border around the layout (pixels).
     * @return this list.
     */
    public Layout setPadding(int padding) {
        this.padding = padding;
        return this;
    }
    
    @Override
    public void update() {
        children.forEach(Tile::update);
    }
    
    /**
     * Horizontally align a tile within the given boundary,
     * subject to the tile's alignment setting.
     * @param tile to align.
     * @param minX minimum x-coordinate of tile's left edge.
     * @param maxX maximum x-coordinate of tile's right edge.
     */
    protected void alignTileHorz(Tile tile, int minX, int maxX) {
        
        //Get min/max bounds for center of tile.
        int min = minX + tile.getWidth()/2;
        int max = maxX - tile.getWidth()/2;
        //Interpolate between bounds using horizontal tile alignment.
        tile.setX(min + (int)((max-min)*tile.getAlignment().H_ALIGNMENT));
    }
    
    /**
     * Vertically align a tile within the given boundary,
     * subject to the tile's alignment setting.
     * @param tile to align.
     * @param minY minimum y-coordinate of tile's bottom edge.
     * @param maxY maximum y-coordinate of tile's top edge.
     */
    protected void alignTileVert(Tile tile, int minY, int maxY) {
        
        //Get min/max bounds for center of tile.
        int min = minY + tile.getHeight()/2;
        int max = maxY - tile.getHeight()/2;
        //Interpolate between bounds using vertical tile alignment.
        tile.setY(min + (int)((max-min)*tile.getAlignment().V_ALIGNMENT));
    }
    
    /**
     * Update aspect ratio of this frame and its children.
     */
    protected void updateAspectRatio() {
        
        //Update aspect ratio for this frame if its set to wrap content.
        if(getFill() == Fill.WRAP_CONTENT_ASPECT) {
            //Use maximum width and height to ensure complete wrap.
            setWidth(Math.max(getWidth(), (int) (getHeight() * getAspectRatio())));
            setHeight(Math.max(getHeight(), (int) (getWidth() / getAspectRatio())));
        }
        
        //Update aspect ratio for each child set to fill parent.
        getChildren().stream()
            .filter(t -> t.getFill() == Fill.FILL_PARENT_ASPECT)
            
            //Use minimum width to prevent overhang.
            .peek(t -> t.setWidth(Math.min(
                t.getWidth(), (int) (t.getHeight() * t.getAspectRatio()))))
            
            //Use minimum height to prevent overhang.
            .forEach(t -> t.setHeight(Math.min(
                t.getHeight(), (int) (t.getWidth() / t.getAspectRatio()))));
    }
}