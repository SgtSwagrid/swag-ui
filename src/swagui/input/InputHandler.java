package swagui.input;

import static org.lwjgl.glfw.GLFW.*;

import swagui.window.Window.Handler;

/**
 * Event handling for user input.
 * @author Alec Dorrington
 */
public class InputHandler implements Handler {
    
    /** Input event handler. */
    EventHandler handler = new EventHandler();
    
    /** Current cursor position. */
    private int mx, my;
    
    /** @return input event handler. */
    public EventHandler getHandler() { return handler; }

    @Override
    public void init(long windowId) {
        
        //Mouse click listener.
        glfwSetMouseButtonCallback(windowId, this::onMouseButton);
        
        //Mouse cursor movement listener.
        glfwSetCursorPosCallback(windowId, this::onCursorPos);
        
        //Key press/release callback.
        glfwSetKeyCallback(windowId, this::onKey);
        
        //Window resize listener.
        glfwSetWindowSizeCallback(windowId, this::onWindowSize);
    }
    
    /**
     * @return current x-coordinate of cursor (pixels, left-to-right).
     */
    public int getMouseX() { return mx; }
    
    /**
     * @return current y-coordinate of cursor (pixels, bottom-to-top).
     */
    public int getMouseY() { return my; }
    
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
            handler.trigger(new MouseLeftClickEvent(x, y));
        } else {
            handler.trigger(new MouseButtonEvent(x, y, button, action));
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
        this.mx = (int)mx - width[0]/2;
        this.my = (int)my - height[0]/2;
        
        //Trigger cursor move event.
        handler.trigger(new MouseMoveEvent(this.mx, this.my));
    }
    
    /**
     * Key press/release listener.
     * @param windowId ID of the window.
     * @param key which was pressed/released.
     * @param scancode
     * @param action press/release.
     * @param mods
     */
    private void onKey(long windowId, int key,
            int scancode, int action, int mods) {
        
        handler.trigger(new KeyboardEvent(key, action));
    }
    
    /**
     * Window resize listener.
     * @param windowId ID of the window.
     * @param width of the window (pixels).
     * @param height of the window (pixels).
     */
    private void onWindowSize(long windowId, int width, int height) {
        
        handler.trigger(new WindowResizeEvent(width, height));
    }
    
    /**
     * Supertype for mouse-related events.
     */
    public abstract class MouseEvent {
        
        /** Coordinates of cursor (pixels, window-space). */
        public final int MX, MY;
        
        private MouseEvent(int mx, int my) {
            MX = mx;
            MY = my;
        }
    }
    
    /**
     * Event for mouse button click/release.
     */
    public class MouseButtonEvent extends MouseEvent {
        
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
    public class MouseLeftClickEvent extends MouseButtonEvent {
        
        private MouseLeftClickEvent(int mx, int my) {
            super(mx, my, GLFW_MOUSE_BUTTON_LEFT, GLFW_PRESS);
        }
    }
    
    /**
     * Event for mouse cursor movement.
     */
    public class MouseMoveEvent extends MouseEvent {
        
        private MouseMoveEvent(int mx, int my) {
            super(mx, my);
        }
    }
    
    /**
     * Event for key press/release.
     */
    public class KeyboardEvent {
        
        /** The key which was used. */
        public final int KEY;
        
        /** Whether the key was pressed/released. */
        public final int ACTION;
        
        private KeyboardEvent(int key, int action) {
            KEY = key;
            ACTION = action;
        }
    }
    
    /**
    * Event for when window is resized by user.
    */
    public class WindowResizeEvent {
    
        /** Size of the window (pixels). */
        public final int WIDTH, HEIGHT;
        
        private WindowResizeEvent(int width, int height) {
            WIDTH = width;
            HEIGHT = height;
        }
    }
}