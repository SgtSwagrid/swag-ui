package swagui.layouts;

import swagui.tiles.Tile;

/**
 * A list of tiles to be displayed consecutively.
 * Places tiles vertically, top-to-bottom.
 * @author Alec Dorrington
 */
public class VerticalList extends Layout {
    
    /** Size of spaces between elements in the list. */
    private int spacing = 0;
    
    /**
     * Create a new vertical list layout.
     * @param children the contents of the list.
     */
    public VerticalList(Tile... children) {
        super(children);
    }
    
    /**
     * @return the size of spaces between elements in the list (pixels).
     */
    public int getSpacing() { return spacing; }
    
    /**
     * @param spacing between elements in the list (pixels).
     * @return this list.
     */
    public Layout setSpacing(int spacing) {
        this.spacing = spacing;
        update();
        return this;
    }
    
    @Override
    public void update() {
        
        updateHeight();
        updateWidth();
        updatePosition();
    }
    
    /**
     * Update heights of this list and its children.
     */
    private void updateHeight() {
        
        //Total amount of vertical padding and spacing.
        int vPadding = (getChildren().size()-1)*getSpacing()
                + 2*getPadding();
        
        //If set to match size of contents vertically.
        if(getVFill() == Fill.WRAP_CONTENT) {
            
            //Update children to obtain their heights.
            getChildren().forEach(Tile::update);
            
            //Height is sum of heights of children, plus padding.
            setHeight(getChildren().stream()
                .mapToInt(Tile::getHeight)
                .sum()
                + vPadding);
            
        } else {
            
            //Sum of heights of absolute-height tiles.
            int solidHeights = getChildren().stream()
                .filter(t -> t.getVFill() != Fill.FILL_PARENT)
                .mapToInt(Tile::getHeight)
                .sum();
            
            //Sum of weights of relative-height tiles.
            int totalWeight = getChildren().stream()
                .filter(t -> t.getVFill() == Fill.FILL_PARENT)
                .mapToInt(Tile::getVWeight)
                .sum();
            
            //Total excess space available to relative-height tiles.
            int stretchyHeights = getHeight() - solidHeights - vPadding;
            
            //Divide excess space among relative-height tiles.
            getChildren().stream()
                .filter(t -> t.getVFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setHeight(
                    stretchyHeights * t.getVWeight() / totalWeight));
        }
    }
    
    /**
     * Update widths of this list and its children.
     */
    private void updateWidth() {
        
        //If set to match size of contents vertically.
        if(getHFill() == Fill.WRAP_CONTENT) {
            
            //Update children to obtains their widths.
            getChildren().forEach(Tile::update);
            
            //Width matches that of widest child, plus padding.
            setWidth(getChildren().stream()
                .mapToInt(Tile::getWidth)
                .max().getAsInt()
                + 2*getPadding());
            
        } else {
            
            //Set relative-width tiles to match width of list.
            getChildren().stream()
                .filter(t -> t.getHFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setWidth(getWidth() - 2*getPadding()));
        }
    }
    
    /**
     * Update positions of this list's children.
     */
    private void updatePosition() {
        
        //Starting y-coordinate at top edge of list.
        int y = getY() + getHeight()/2 - getPadding();
        
        //Sum of heights of absolute-height tiles.
        int solidHeight = getChildren().stream()
            .filter(t -> t.getVFill() != Fill.FILL_PARENT)
            .mapToInt(Tile::getHeight)
            .sum();
        
        //Unfilled height per tile.
        int excessPadding = 0;
        
        //If there are no relative-width tiles.
        if(getChildren().stream().noneMatch(
                t -> t.getVFill() == Fill.FILL_PARENT)) {
            
            //Total amount of horizontal padding and spacing.
            int vPadding = (getChildren().size()-1)*getSpacing()
                    + 2*getPadding();
            
            //Total amount of extra unfilled width.
            int excessHeight = getHeight() - solidHeight - vPadding;
            excessPadding = excessHeight / getChildren().size();
        }
        
        for(Tile tile : getChildren()) {
            
            //Align each tile within the cell.
            alignTileVert(tile, y-tile.getHeight()-excessPadding, y);
            alignTileHorz(tile, getMinX()+getPadding(),
                    getMaxX()-getPadding());
            
            //Increment y value for next tile.
            y -= tile.getHeight() + excessPadding + getSpacing();
        }
    }
}