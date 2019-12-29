package swagui.window;

import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;

import swagui.graphics.Colour;

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
     * @param scene to display.
     * @param handler for use in handling input events.
     */
    public Window(int width, int height, String title,
            Scene scene, Handler handler) {
        long windowId = init(width, height, title, scene, handler);
        run(windowId, scene, handler);
        close(windowId, scene, handler);
    }
    
    /**
     * Initialize and create a new window.
     * @param width of window (pixels).
     * @param height of window (pixels).
     * @param title of window.
     * @param scene to display.
     * @param handler for use in handling input events.
     * @return the window ID.
     */
    private long init(int width, int height, String title,
            Scene scene, Handler handler) {
        
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
        
        //Initialize OpenGL settings.
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        //Initialize shader.
        handler.init(windowId);
        scene.init(width, height, handler);
        return windowId;
    }
    
    /**
     * Continually update window until close.
     * @param windowId of GLFW window.
     * @param scene to display.
     * @param handler for use in handling input events.
     */
    private void run(long windowId, Scene scene, Handler handler) {
        
        //Continue rendering while window is being resized.
        glfwSetFramebufferSizeCallback(windowId, (window, w, h) -> {
            glViewport(0, 0, w, h);
            render(windowId, scene);
        });
        
        //Update window and event handler until window is closed.
        while(!glfwWindowShouldClose(windowId)) {
            
            render(windowId, scene);
            glfwPollEvents();
        }
    }
    
    /**
     * Render a frame to screen.
     * @param windowId ID of window.
     * @param scene to display.
     */
    private void render(long windowId, Scene scene) {
        
        //Clear colour/depth buffers.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Colour colour = scene.getColour();
        glClearColor(colour.R/255.0F, colour.G/255.0F, colour.B/255.0F, 1.0F);
        
        //Get window size.
        int[] width = new int[1], height = new int[1];
        glfwGetWindowSize(windowId, width, height);
        
        //Perform render.
        scene.render(width[0], height[0]);
        glfwSwapBuffers(windowId);
    }
    
    /**
     * Close the window.
     * @param windowId of GLFW window.
     * @param scene to display.
     * @param handler for use in handling input events.
     */
    private void close(long windowId, Scene scene, Handler handler) {
        //Destroy shader and window.
        scene.destroy();
        glfwDestroyWindow(windowId);
    }
    
    /**
     * Scene to render to screen.
     */
    public interface Scene {
        
        /**
         * Initialize scene shaders.
         * @param width of the window (pixels).
         * @param height of the window (pixels).
         * @param handler of window input events.
         */
        public void init(int width, int height, Handler handler);
        
        /**
         * Render the scene to the screen.
         * @param width of the window (pixels).
         * @param height of the window (pixels).
         */
        public void render(int width, int height);
        
        /**
         * @return the background colour of this scene.
         */
        public Colour getColour();
        
        /**
         * Perform cleanup upon window close.
         */
        public void destroy();
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
}