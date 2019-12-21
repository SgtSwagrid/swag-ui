package swagui.layouts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * Superclass for layouts, which automatic tile placement.
 * @author Alec Dorrington
 */
public class Layout extends Tile {
    
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
        setFill(Fill.FILL_PARENT, Fill.FILL_PARENT);
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
        tile.setScene(getScene());
        if(getScene() != null) getScene().update();
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
        tile.setScene(getScene());
        if(getScene() != null) getScene().update();
        return this;
    }
    
    /**
     * Remove a tile from the layout.
     * @param tile to remove.
     * @return this layout.
     */
    public Layout removeTile(Tile tile) {
        children.remove(tile);
        if(getScene() != null) getScene().update();
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
        if(getScene() != null) getScene().update();
        return this;
    }
    
    @Override
    public Layout setScene(Scene2D scene) {
        super.setScene(scene);
        children.forEach(t -> t.setScene(scene));
        if(getScene() != null) getScene().update();
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
        
        switch(tile.getAlignment()) {
        
        //Tile aligns with left edge of list.
        case BOTTOM_LEFT: case LEFT: case TOP_LEFT:
            tile.setX(minX + tile.getWidth()/2);
            break;
        
        //Tile aligns with center of list.
        case BOTTOM: case CENTER: case TOP:
            tile.setX((minX + maxX)/2);
            break;
        
        //Tile aligns with right edge of list.
        case BOTTOM_RIGHT: case RIGHT: case TOP_RIGHT:
            tile.setX(maxX - tile.getWidth()/2);
            break;
        }
    }
    
    /**
     * Vertically align a tile within the given boundary,
     * subject to the tile's alignment setting.
     * @param tile to align.
     * @param minY minimum y-coordinate of tile's bottom edge.
     * @param maxY maximum y-coordinate of tile's top edge.
     */
    protected void alignTileVert(Tile tile, int minY, int maxY) {
        
        switch(tile.getAlignment()) {
        
        //Tile aligns with bottom edge of list.
        case BOTTOM_LEFT: case BOTTOM: case BOTTOM_RIGHT:
            tile.setY(minY + tile.getHeight()/2);
            break;
        
        //Tile aligns with center of list.
        case LEFT: case CENTER: case RIGHT:
            tile.setY((minY + maxY)/2);
            break;
        
        //Tile aligns with top edge of list.
        case TOP_LEFT: case TOP: case TOP_RIGHT:
            tile.setY(maxY - tile.getHeight()/2);
            break;
        }
    }
}