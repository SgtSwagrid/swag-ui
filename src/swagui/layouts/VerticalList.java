package swagui.layouts;

import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * A list of tiles to be displayed consecutively.
 * Places tiles vertically, top-to-bottom.
 * @author Alec Dorrington
 */
public class VerticalList extends ListLayout {
    
    /**
     * Create a new vertical list layout.
     * @param scene in which the list exists.
     * @param children the contents of the list.
     */
    public VerticalList(Scene2D scene, Tile... children) {
        super(scene, children);
    }
    
    @Override
    public void update() {
        updateHeight();
        updateWidth();
        updateY();
        updateX();
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
     * Update y-coordinate of this list's children.
     */
    private void updateY() {
        
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
        
        for(Tile t : getChildren()) {
            
            switch(t.getAlignment()) {
            
            //Tile aligns with top edge of cell.
            case TOP_LEFT: case TOP: case TOP_RIGHT:
                t.setY(y - t.getHeight()/2);
                break;
            
            //Tile aligns with center of cell.
            case LEFT: case CENTER: case RIGHT:
                t.setY(y - t.getHeight()/2 - excessPadding/2);
                break;
            
            //Tile aligns with bottom edge of cell.
            case BOTTOM_LEFT: case BOTTOM: case BOTTOM_RIGHT:
                t.setY(y - t.getHeight()/2 - excessPadding);
                break;
            }
            //Increment y value for next tile.
            y -= t.getHeight() + excessPadding + getSpacing();
        }
    }
    
    /**
     * Update x-coordinate of this list's children.
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
}