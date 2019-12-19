package swagui.layouts;

import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * A list of tiles to be displayed consecutively.
 * Places tiles horizontally, left-to-right.
 * @author Alec Dorrington
 */
public class HorizontalList extends ListLayout {
    
    /**
     * Create a new horizontal list layout.
     * @param scene in which the list exists.
     * @param children the contents of the list.
     */
    public HorizontalList(Scene2D scene, Tile... children) {
        super(scene, children);
    }
    
    @Override
    public void update() {
        updateWidth();
        updateHeight();
        updateX();
        updateY();
    }
    
    /**
     * Update widths of this list and its children.
     */
    private void updateWidth() {
        
        //Total amount of horizontal padding and spacing.
        int hPadding = (getChildren().size()-1)*getSpacing()
                + 2*getPadding();
        
        //If set to match size of contents horizontally.
        if(getHFill() == Fill.WRAP_CONTENT) {
            
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
                .filter(t -> t.getHFill() != Fill.FILL_PARENT)
                .mapToInt(Tile::getWidth)
                .sum();
            
            //Sum of weights of relative-width tiles.
            int totalWeight = getChildren().stream()
                .filter(t -> t.getHFill() == Fill.FILL_PARENT)
                .mapToInt(Tile::getHWeight)
                .sum();
            
            //Total excess space available to relative-width tiles.
            int stretchyWidth = getWidth() - solidWidth - hPadding;
            
            //Divide excess space among relative-width tiles.
            getChildren().stream()
                .filter(t -> t.getHFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setWidth(
                    stretchyWidth * t.getHWeight() / totalWeight));
        }
    }
    
    /**
     * Update heights of this list and its children.
     */
    private void updateHeight() {
        
        //If set to match size of contents vertically.
        if(getVFill() == Fill.WRAP_CONTENT) {
            
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
                .filter(t -> t.getVFill() == Fill.FILL_PARENT)
                .forEach(t -> t.setHeight(getHeight() - 2*getPadding()));
        }
    }
    
    /**
     * Update x-coordinate of this list's children.
     */
    private void updateX() {
        
        //Starting x-coordinate at left edge of list.
        int x = getX() - getWidth()/2 + getPadding();
        
        //Sum of widths of absolute-width tiles.
        int solidWidth = getChildren().stream()
            .filter(t -> t.getHFill() != Fill.FILL_PARENT)
            .mapToInt(Tile::getWidth)
            .sum();
        
        //Unfilled width per tile.
        int excessPadding = 0;
        
        //If there are no relative-width tiles.
        if(getChildren().stream().noneMatch(
                t -> t.getHFill() == Fill.FILL_PARENT)) {
            
            //Total amount of horizontal padding and spacing.
            int hPadding = (getChildren().size()-1)*getSpacing()
                    + 2*getPadding();
            
            //Total amount of extra unfilled width.
            int excessWidth = getWidth() - solidWidth - hPadding;
            excessPadding = excessWidth / getChildren().size();
        }
        
        for(Tile t : getChildren()) {
            
            switch(t.getAlignment()) {
            
            //Tile aligns with left edge of cell.
            case BOTTOM_LEFT: case LEFT: case TOP_LEFT:
                t.setX(x + t.getWidth()/2);
                break;
            
            //Tile aligns with center of cell.
            case BOTTOM: case CENTER: case TOP:
                t.setX(x + t.getWidth()/2 + excessPadding/2);
                break;
            
            //Tile aligns with right edge of cell.
            case BOTTOM_RIGHT: case RIGHT: case TOP_RIGHT:
                t.setX(x + t.getWidth()/2 + excessPadding);
                break;
            }
            //Increment x value for next tile.
            x += t.getWidth() + excessPadding + getSpacing();
        }
    }
    
    /**
     * Update y-coordinate of this list's children.
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