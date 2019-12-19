package swagui.layouts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * A list of tiles to be displayed consecutively.
 * Supertype for HorizontalList and VerticalList.
 * @author Alec Dorrington
 */
public abstract class ListLayout extends Tile {
    
    /** Child tiles in this layout. */
    private List<Tile> children = new LinkedList<>();
    
    /** Size of the border around the list. */
    private int padding = 0;
    
    /** Size of spaces between elements in the list. */
    private int spacing = 0;
    
    /**
     * Create a new list layout.
     * @param scene in which the list exists.
     * @param children the contents of the list.
     */
    public ListLayout(Scene2D scene, Tile... children) {
        super(scene);
        for(Tile tile : children) this.children.add(tile);
        setFill(Fill.FILL_PARENT, Fill.FILL_PARENT);
        setVisible(false);
        setDepth(1);
        update();
    }
    
    /**
     * Add a new tile to end of the list.
     * @param tile to add.
     * @return this list.
     */
    public ListLayout addTile(Tile tile) {
        children.add(tile);
        update();
        return this;
    }
    
    /**
     * Add a new tile to the list.
     * @param tile to add.
     * @param index position at which tile is inserted.
     * @return this list.
     */
    public ListLayout addTile(Tile tile, int index) {
        children.add(index, tile);
        update();
        return this;
    }
    
    /**
     * Remove a tile from the list.
     * @param tile to remove.
     * @return this list.
     */
    public ListLayout removeTile(Tile tile) {
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
     * @return the size of the border around the list (pixels).
     */
    public int getPadding() { return padding; }
    
    /**
     * @param padding size of border around the list (pixels).
     * @return this list.
     */
    public ListLayout setPadding(int padding) {
        this.padding = padding;
        update();
        return this;
    }
    
    /**
     * @return the size of spaces between elements in the list (pixels).
     */
    public int getSpacing() { return spacing; }
    
    /**
     * @param spacing between elements in the list (pixels).
     * @return this list.
     */
    public ListLayout setSpacing(int spacing) {
        this.spacing = spacing;
        update();
        return this;
    }
}