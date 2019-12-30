package swagui.layouts;

import swagui.tiles.Tile;

/**
 * A list of tiles to be displayed consecutively.
 * Places tiles horizontally, left-to-right.
 * @author Alec Dorrington
 */
public class HorizontalList extends Layout {
    
    /** Size of spaces between elements in the list. */
    private int spacing = 0;
    
    /**
     * Create a new horizontal list layout.
     * @param children the contents of the list.
     */
    public HorizontalList(Tile... children) {
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
    public HorizontalList setSpacing(int spacing) {
        this.spacing = spacing;
        update();
        return this;
    }
    
    @Override
    public void update() {
        
        //Update sizes of this list and its children.
        updateWidth();
        updateHeight();
        updateAspectRatio();
        
        //Update positions of list frame and its children.
        updatePosition();
        
        getChildren().forEach(Tile::update);
    }
    
    /**
     * Update widths of this list and its children.
     */
    private void updateWidth() {
        
        //Total amount of horizontal padding and spacing.
        int hPadding = (getChildren().size()-1)*getSpacing()
                + 2*getPadding();
        
        //If set to match size of contents horizontally.
        if(getFill().H_WRAP_CONTENT) {
            
            //Update children to obtain their widths.
            getChildren().forEach(Tile::update);
            
            //Width is sum of widths of children, plus padding.
            setWidth(getChildren().stream()
                .mapToInt(Tile::getWidth)
                .sum()
                + hPadding);
            
        } else {
            
            //Sum of widths of absolute-width tiles.
            int solidWidth = getChildren().stream()
                .filter(t -> !t.getFill().H_FILL_PARENT)
                .mapToInt(Tile::getWidth)
                .sum();
            
            //Sum of weights of relative-width tiles.
            int totalWeight = getChildren().stream()
                .filter(t -> t.getFill().H_FILL_PARENT)
                .mapToInt(Tile::getHWeight)
                .sum();
            
            //Total excess space available to relative-width tiles.
            int stretchyWidth = getWidth() - solidWidth - hPadding;
            
            //Divide excess space among relative-width tiles.
            getChildren().stream()
                .filter(t -> t.getFill().H_FILL_PARENT)
                .forEach(t -> t.setWidth(
                    stretchyWidth * t.getHWeight() / totalWeight));
        }
    }
    
    /**
     * Update heights of this list and its children.
     */
    private void updateHeight() {
        
        //If set to match size of contents vertically.
        if(getFill().V_WRAP_CONTENT) {
            
            //Update children to obtains their heights.
            getChildren().forEach(Tile::update);
            
            //Height matches that of tallest child, plus padding.
            setHeight(getChildren().stream()
                .mapToInt(Tile::getHeight)
                .max().getAsInt()
                + 2*getPadding());
            
        } else {
            
            //Set relative-height tiles to match height of list.
            getChildren().stream()
                .filter(t -> t.getFill().V_FILL_PARENT)
                .forEach(t -> t.setHeight(getHeight() - 2*getPadding()));
        }
    }
    
    /**
     * Update positions of this list's children.
     */
    private void updatePosition() {
        
        //Starting x-coordinate at left edge of list.
        int x = getX() - getWidth()/2 + getPadding();
        
        //Sum of widths of absolute-width tiles.
        int solidWidth = getChildren().stream()
            .filter(t -> !t.getFill().H_FILL_PARENT)
            .mapToInt(Tile::getWidth)
            .sum();
        
        //Unfilled width per tile.
        int excessPadding = 0;
        
        //If there are no relative-width tiles.
        if(getChildren().stream().noneMatch(
                t -> t.getFill().H_FILL_PARENT)) {
            
            //Total amount of horizontal padding and spacing.
            int hPadding = (getChildren().size()-1)*getSpacing()
                    + 2*getPadding();
            
            //Total amount of extra unfilled width.
            int excessWidth = getWidth() - solidWidth - hPadding;
            excessPadding = excessWidth / getChildren().size();
        }
        
        for(Tile tile : getChildren()) {
            
            //Align each tile within the cell.
            alignTileHorz(tile, x, x+tile.getWidth()+excessPadding);
            alignTileVert(tile, getMinY()+getPadding(),
                    getMaxY()-getPadding());
            
            //Increment x value for next tile.
            x += tile.getWidth() + excessPadding + getSpacing();
        }
    }
}