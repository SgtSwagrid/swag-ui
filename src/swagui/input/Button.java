package swagui.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import java.util.function.BiConsumer;

import swagui.graphics.Gradient;
import swagui.input.InputHandler.MouseButtonEvent;
import swagui.input.InputHandler.MouseMoveEvent;
import swagui.layouts.Frame;

/**
 * A tile with click handling.
 * @author Alec Dorrington
 */
public class Button extends Frame {
    
    /** Input event handler from window. */
    private InputHandler input;
    
    /** Button event handler. */
    EventHandler handler = new EventHandler();
    
    /** Whether the cursor is currently over this button. */
    private boolean mouseOver = false;
    
    /**
     * Create a new button.
     * @param input event handler.
     */
    public Button(InputHandler input) {
        super();
        this.input = input;
        init();
    }
    
    /**
     * Create a new button.
     * @param input event handler.
     * @param x x-coordinate of the button (pixels).
     * @param y y-coordinate of the button (pixels).
     * @param width of the button (pixels, left-to-right).
     * @param height of the button (pixels, bottom-to-top).
     */
    public Button(InputHandler input, int x, int y, int width, int height) {
        super();
        this.input = input;
        setPosition(x, y);
        setSize(width, height);
        init();
    }
    
    /**
     * Perform an action whenever the button is left-clicked.
     * Accepts the coordinates of the cursor (pixels, relative to button center).
     * @param action to be performed upon button left-click.
     * @return this button.
     */
    public Button onClick(BiConsumer<Integer, Integer> action) {
        getHandler().register(ButtonLeftClickEvent.class,
            e -> action.accept(e.BX, e.BY));
        return this;
    }
    
    /** @return button event handler. */
    public EventHandler getHandler() { return handler; }
    
    /**  @return whether the cursor is currently over this button. */
    public boolean isMouseOver() { return mouseOver; }
    
    @Override
    public void update() {
        updateCursor(input.getMouseX(), input.getMouseY());
        super.update();
    }
    
    @Override
    public Gradient getColour() {
        //Darken button when cursor is over.
        return super.getColour().darken(isMouseOver() ? 15 : 0);
    }
    
    /**
     * Button listener initialization.
     */
    private void init() {
        
        //Button click event handler.
        input.getHandler().register(MouseButtonEvent.class, this, e -> {
            
            if(mouseOver) {
                //Trigger mouse click event.
                if(e.BUTTON == GLFW_MOUSE_BUTTON_LEFT && e.ACTION == GLFW_PRESS) {
                    handler.trigger(new ButtonLeftClickEvent(
                        e.MX-getX(), e.MY-getY()));
                } else {
                    handler.trigger(new ButtonClickEvent(
                        e.MX-getX(), e.MY-getY(), e.BUTTON, e.ACTION));
                }
            }
        });
        
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
         
        //Trigger mouse enter/leave events as appropriate.
        if(!mouseOver && mouseStillOver) {
            handler.trigger(new ButtonMouseEnterEvent(mx-getX(), my-getY()));
        } else if(mouseOver && !mouseStillOver) {
            handler.trigger(new ButtonMouseLeaveEvent(mx-getX(), my-getY()));
        }
        mouseOver = mouseStillOver;
    }
    
    /**
     * Event for button interaction.
     */
    public abstract class ButtonEvent {
        
        /** Cursor position (pixels, relative to button center). */
        public final int BX, BY;
        
        private ButtonEvent(int bx, int by) {
            BX = bx;
            BY = by;
        }
    }
    
    /**
     * Event for button click.
     */
    public class ButtonClickEvent extends ButtonEvent {
        
        /** The ID of the button which was clicked. */
        public final int BUTTON;
        
        /** Click/release. */
        public final int ACTION;
        
        private ButtonClickEvent(int bx, int by, int button, int action) {
            super(bx, by);
            BUTTON = button;
            ACTION = action;
        }
    }
    
    /**
     * Event for button left-click.
     */
    public class ButtonLeftClickEvent extends ButtonClickEvent {
        
        private ButtonLeftClickEvent(int mx, int my) {
            super(mx, my, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS);
        }
    }
    
    /**
     * Event for cursor entering button.
     */
    public class ButtonMouseEnterEvent extends ButtonEvent {
        
        private ButtonMouseEnterEvent(int mx, int my) {
            super(mx, my);
        }
    }
    
    /**
     * Event for cursor leaving button.
     */
    public class ButtonMouseLeaveEvent extends ButtonEvent {
        
        private ButtonMouseLeaveEvent(int mx, int my) {
            super(mx, my);
        }
    }
}