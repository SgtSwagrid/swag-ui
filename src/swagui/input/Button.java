package swagui.input;

import swagui.input.InputHandler.MouseLeftClickEvent;
import swagui.input.InputHandler.MouseMoveEvent;
import swagui.tiles.Scene2D;
import swagui.tiles.Tile;

/**
 * A tile with click handling.
 * @author Alec Dorrington
 */
public class Button extends Tile {
    
    /** Input event handler. */
    private InputHandler input;
    
    /** Whether the cursor is currently over this button. */
    private boolean mouseOver = false;
    
    /**
     * Create a new button.
     * @param scene in which the tile exists.
     * @param input event handler.
     */
    public Button(Scene2D scene, InputHandler input) {
        super(scene);
        this.input = input;
        init();
    }
    
    /**
     * Create a new button.
     * @param scene in which the tile exists.
     * @param input event handler.
     * @param x x-coordinate of the button (pixels).
     * @param y y-coordinate of the button (pixels).
     * @param width of the button (pixels, left-to-right).
     * @param height of the button (pixels, bottom-to-top).
     */
    public Button(Scene2D scene, InputHandler input,
            int x, int y, int width, int height) {
        super(scene, x, y, width, height);
        this.input = input;
        init();
    }
    
    /** @return input event handler. */
    public InputHandler getInputHandler() { return input; }
    
    /**  @return whether the cursor is currently over this button. */
    public boolean isMouseOver() { return mouseOver; }
    
    /**
     * Button listener initialization.
     */
    private void init() {
        
        //Left-click event handler.
        input.getHandler().register(MouseLeftClickEvent.class, this,
            e -> { if(mouseOver) onLeftClick(); });
        
        //Mouse movement event handler.
        input.getHandler().register(MouseMoveEvent.class, this,
            e -> updateCursor(e.MX, e.MY));
    }
    
    /**
     * Update this button regarding cursor position.
     * @param mx x-coordinate of cursor (pixels, window-space).
     * @param my y-coordinate of cursor (pixels, window-space).
     */
    private void updateCursor(int mx, int my) {
        
        //Determine if cursor is over the button.
        boolean mouseStillOver = mx >= getMinX() && mx <= getMaxX() &&
                                 my >= getMinY() && my <= getMaxY();
         
        //Call mouse enter/leave as appropriate.
        if(!mouseOver && mouseStillOver) onMouseEnter();
        else if(mouseOver && !mouseStillOver) onMouseLeave();
        mouseOver = mouseStillOver;
    }
    
    /**
     * Called when the button is left-clicked.
     */
    protected void onLeftClick() {}
    
    /**
     * Called when the cursor enters the button.
     */
    protected void onMouseEnter() {}
    
    /**
     * Called when the cursor leaves the button.
     */
    protected void onMouseLeave() {}
    
    @Override
    public void delete() {
        super.delete();
        input.getHandler().remove(this);
    }
}