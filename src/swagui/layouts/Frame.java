package swagui.layouts;

import swagui.tiles.Tile;

/**
 * A frame layout into which other tiles may be placed.
 * @author Alec Dorrington
 */
public class Frame extends Layout {
    
    /**
     * Create a new frame layout
     * @param children the contents of the frame.
     */
    public Frame(Tile... children) {
        super(children);
        setVisible(true);
        setDepth(2);
    }
    
    @Override
    public void update() {
        
        //Update sizes of this frame and its children.
        updateWidth();
        updateHeight();
        updateAspectRatio();
        
        //Update positions of this frame and its children.
        updatePosition();
        
        getChildren().forEach(Tile::update);
    }
    
    /**
     * Update widths of this frame and its children.
     */
    private void updateWidth() {
        
        //If set to match size of contents horizontally.
        if(getFill().H_WRAP_CONTENT) {
            
            //Set width of frame to match widest child.
            int width = getChildren().stream()
                .peek(Tile::update)
                .mapToInt(Tile::getWidth)
                .max().getAsInt();
            setWidth(width + 2*getPadding());
            
        } else {
            
            //Set relative-width tiles to match width of frame.
            getChildren().stream()
                .filter(t -> t.getFill().H_FILL_PARENT)
                .forEach(t -> t.setWidth(getWidth() - 2*getPadding()));
        }
    }
    
    /**
     * Update heights of this frame and its children.
     */
    private void updateHeight() {
        
        //If set to match size of contents vertically.
        if(getFill().V_WRAP_CONTENT) {
            
            //Set height of frame to match tallest child.
            int height = getChildren().stream()
                .mapToInt(Tile::getHeight)
                .max().getAsInt();
            setHeight(height + 2*getPadding());
            
        } else {
            
            //Set relative-height tiles to match height of frame.
            getChildren().stream()
                .filter(t -> t.getFill().V_FILL_PARENT)
                .forEach(t -> t.setHeight(getHeight() - 2*getPadding()));
        }
    }
    
    /**
     * Update size of this frame's children.
     */
    private void updatePosition() {
        
        getChildren().forEach(tile -> {
            
            //Align each tile horizontally within the frame.
            alignTileHorz(tile,
                getMinX()+getPadding(),
                getMaxX()-getPadding());
            
            //Align each tile vertically within the frame.
            alignTileVert(tile,
                getMinY()+getPadding(),
                getMaxY()-getPadding());
        });
    }
}