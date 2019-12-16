package swagui.input;

import static org.lwjgl.glfw.GLFW.*;

import strategybots.games.graphics.Window.Handler;

/**
 * Event handling for user input.
 * @author Alec Dorrington
 */
public class InputHandler implements Handler {
    
    /** Singleton input handler instance. */
    public static final InputHandler INSTANCE = new InputHandler();

    @Override
    public void init(long windowId) {
        
        //Mouse click listener.
        glfwSetMouseButtonCallback(windowId, this::onMouseButton);
        
        //Mouse move listener.
        glfwSetCursorPosCallback(windowId, this::onCursorPos);
    }
    
    /**
     * Mouse click listener.
     * @param windowId ID of window.
     * @param button ID of button clicked.
     * @param action click/release.
     * @param mods
     */
    private void onMouseButton(long windowId, int button, int action, int mods) {
        
        //Get size of window.
        int[] width = new int[1], height = new int[1];
        glfwGetWindowSize(windowId, width, height);
        
        //Get cursor position (corner-coordinates).
        double[] mx = new double[1], my = new double[1];
        glfwGetCursorPos(windowId, mx, my);
        
        //Calculate cursor position (center-coordinates).
        int x = (int)mx[0] - width[0]/2;
        int y = (int)my[0] - height[0]/2;
        
        //Trigger mouse click event.
        if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            new MouseLeftClickEvent(x, y);
        } else {
            new MouseButtonEvent(x, y, button, action);
        }
    }
    
    /**
     * Mouse cursor movement listener.
     * @param windowId ID of the window.
     * @param mx x-coordinate of the cursor (pixels, window-space).
     * @param my y-coordinate of the cursor (pixels, window-space).
     */
    private void onCursorPos(long windowId, double mx, double my) {
        
        //Get size of window.
        int[] width = new int[1], height = new int[1];
        glfwGetWindowSize(windowId, width, height);
        
        //Calculate cursor position (center-coordinates).
        int x = (int)mx - width[0]/2;
        int y = (int)my - height[0]/2;
        
        //Trigger cursor move event.
        new MouseMoveEvent(x, y);
    }
    
    /**
     * Supertype for mouse-related events.
     */
    public static abstract class MouseEvent {
        
        /** Coordinates of cursor (pixels, window-space). */
        public final int MX, MY;
        
        private MouseEvent(int mx, int my) {
            MX = mx;
            MY = my;
            EventHandler.trigger(this);
        }
    }
    
    /**
     * Event for mouse button click/release.
     */
    public static class MouseButtonEvent extends MouseEvent {
        
        /** The ID of the button which was clicked. */
        public final int BUTTON;
        
        /** Click/release. */
        public final int ACTION;
        
        private MouseButtonEvent(int mx, int my, int button, int action) {
            super(mx, my);
            BUTTON = button;
            ACTION = action;
        }
    }
    
    /**
     * Event for mouse left-click.
     */
    public static class MouseLeftClickEvent extends MouseButtonEvent {
        
        private MouseLeftClickEvent(int mx, int my) {
            super(mx, my, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS);
        }
    }
    
    /**
     * Event for mouse cursor movement.
     */
    public static class MouseMoveEvent extends MouseEvent {
        
        private MouseMoveEvent(int mx, int my) {
            super(mx, my);
        }
    }
}