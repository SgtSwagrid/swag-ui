package swagui.tiles;

import java.util.SortedSet;
import java.util.TreeSet;

import swagui.graphics.Colour;
import swagui.graphics.Gradient;
import swagui.input.InputHandler;
import swagui.input.InputHandler.WindowResizeEvent;
import swagui.layouts.Frame;
import swagui.layouts.Layout;
import swagui.shaders.TileShader;
import swagui.window.Window.Handler;
import swagui.window.Window.Scene;

/**
 * Scene of 2D tiles to display in window.
 * @author Alec Dorrington
 */
public class Scene2D implements Scene {
    
    /** Scene tile shader. */
    TileShader shader = new TileShader();
    
    /**  Set of all tiles, ordered by depth. */
    private SortedSet<Tile> tiles = new TreeSet<Tile>(
            (t1, t2) -> 2*(t1.getDepth()-t2.getDepth())+1);
    
    /** Background of scene. */
    private Frame background = (Frame) new Frame()
            .setDepth(0)
            .setVisible(true)
            .setColour(Colour.PICO_VOID);
    
    /** Root layout of scene. */
    private Layout root = new Layout(background);

    @Override
    public void init(int width, int height, Handler handler) {
        
        //Scene2D requires an associated InputHandler.
        if(!(handler instanceof InputHandler))
            throw new IllegalStateException("Missing InputHandler.");
        
        //Initialize shader.
        shader.init();
        
        //Initialize background.
        background.setSize(width, height);
        update();
        
        //Resize background upon window resize.
        InputHandler input = (InputHandler) handler;
        input.getHandler().register(WindowResizeEvent.class, e -> {
            background.setSize(e.WIDTH, e.HEIGHT);
            update();
        });
    }

    @Override
    public void render(int width, int height) {
        shader.render(tiles, width, height);
    }
    
    @Override
    public Gradient getColour() {
        return background.getColour();
    }

    @Override
    public void destroy() {
        shader.destroy();
    }
    
    /**
     * Get the scene root, from which all tiles descend.
     * @return the root tile of the screen.
     */
    public Layout getRoot() { return root; }
    
    /**
     * Get the scene background, a FrameLayout sized to match the window.
     * @return the background tile of the screen.
     */
    public Frame getBackground() { return background; }
    
    /**
     * Update this scene and all of its children.
     * @return this scene.
     */
    public Scene2D update() {
        
        //Update all tiles.
        tiles.clear();
        root.update();
        tiles.addAll(root.getAncestors());
        return this;
    }
}