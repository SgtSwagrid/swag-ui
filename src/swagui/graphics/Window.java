package swagui.graphics;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import strategybots.games.input.EventHandler;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.glfw.GLFWErrorCallback.*;

/**
 * A GLFW/OpenGL window.
 * @author Alec Dorrington
 */
public class Window {
    
    /**
     * Create and run a new window, blocking until exit.
     * @param width of window (pixels).
     * @param height of window (pixels).
     * @param title of window.
     * @param shader for use in rendering.
     * @param handler for use in handling input events.
     */
    public Window(int width, int height, String title,
            Shader shader, Handler handler) {
        long windowId = init(width, height, title, shader, handler);
        run(windowId, shader, handler);
        close(windowId, shader, handler);
    }
    
    /**
     * Initialize and create a new window.
     * @param width of window (pixels).
     * @param height of window (pixels).
     * @param title of window.
     * @param shader for use in rendering.
     * @param handler for use in handling input events.
     * @return the window ID.
     */
    private long init(int width, int height, String title,
            Shader shader, Handler handler) {
        
        //Initialize GLFW.
        glfwInit();
        glfwSetErrorCallback(createPrint(System.err));
        
        //Set version details.
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        //Create and show window.
        long windowId = glfwCreateWindow(width, height, title, 0, 0);
        glfwMakeContextCurrent(windowId);
        createCapabilities();
        glfwShowWindow(windowId);
        
        //Enable window resize event.
        glfwSetFramebufferSizeCallback(windowId, (window, w, h) ->
            new WindowResizeEvent(w, h));
        
        //Setup OpenGL viewport, updating upon resize.
        glViewport(0, 0, width, height);
        EventHandler.register(WindowResizeEvent.class, this, e -> {
            glViewport(0, 0, e.WIDTH, e.HEIGHT);
            render(windowId, shader);
        });
        
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //Initialize shader.
        shader.doInit();
        handler.init(windowId);
        return windowId;
    }
    
    /**
     * Continually update window until close.
     * @param windowId of GLFW window.
     * @param shader for use in rendering.
     * @param handler for use in handling input events.
     */
    private void run(long windowId, Shader shader, Handler handler) {
        
        //Update window and event handler until window is closed.
        while(!glfwWindowShouldClose(windowId)) {
            
            render(windowId, shader);
            glfwPollEvents();
        }
    }
    
    /**
     * Render a frame to screen.
     * @param windowId ID of window.
     * @param shader shader with which to render.
     */
    private void render(long windowId, Shader shader) {
        
        //Clear buffers.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
        glClearColor(0.5F, 0.5F, 0.5F, 1.0F);
        
        //Get window size.
        int[] width = new int[1], height = new int[1];
        glfwGetWindowSize(windowId, width, height);
        
        //Perform render.
        shader.doRender(width[0], height[0]);
        glfwSwapBuffers(windowId);
    }
    
    /**
     * Close the window.
     * @param windowId of GLFW window.
     * @param shader for use in rendering.
     * @param handler for use in handling input events.
     */
    private void close(long windowId, Shader shader, Handler handler) {
        //Destroy shader and window.
        shader.doDestroy();
        glfwDestroyWindow(windowId);
        EventHandler.remove(this);
    }
    
    /**
     * Handler of window input events.
     */
    public interface Handler {
        
        /**
         * Initialize input listeners.
         * @param window window to listen to.
         */
        public void init(long windowId);
    }
    
    /**
     * Event for when window is resized by user.
     */
    public static class WindowResizeEvent {
        
        /** Size of the window (pixels). */
        public final int WIDTH, HEIGHT;
        
        private WindowResizeEvent(int width, int height) {
            WIDTH = width;
            HEIGHT = height;
            EventHandler.trigger(this);
        }
    }
}