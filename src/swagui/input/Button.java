package swagui.input;

import strategybots.games.graphics.Tile;
import strategybots.games.input.InputHandler.MouseLeftClickEvent;
import strategybots.games.input.InputHandler.MouseMoveEvent;

/**
 * A tile with click handling.
 * @author Alec Dorrington
 */
public class Button extends Tile {
    
    /** Whether the cursor is currently over this button. */
    private boolean mouseOver = false;
    /**  @return Whether the cursor is currently over this button. */
    public boolean isMouseOver() { return mouseOver; }
    
    /**
     * Create a new button.
     * @param x x-coordinate of the button (pixels).
     * @param y y-coordinate of the button (pixels).
     * @param width of the button (pixels, left-to-right).
     * @param height of the button (pixels, bottom-to-top).
     */
    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
        init();
    }
    
    /**
     * Button listener initialization.
     */
    private void init() {
        
        //Left-click event handler.
        EventHandler.register(MouseLeftClickEvent.class, this,
            e -> { if(mouseOver) onLeftClick(); });
        
        //Mouse movement event handler.
        EventHandler.register(MouseMoveEvent.class, this,
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
        EventHandler.remove(this);
    }
}