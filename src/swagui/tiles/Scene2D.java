package swagui.tiles;

import java.util.SortedSet;
import java.util.TreeSet;

import swagui.graphics.Colour;
import swagui.input.InputHandler;
import swagui.input.InputHandler.WindowResizeEvent;
import swagui.layouts.FrameLayout;
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
    private FrameLayout background = (FrameLayout) new FrameLayout(this)
            .setDepth(0)
            .setVisible(true)
            .setColour(Colour.ELECTROMAGNETIC);

    @Override
    public void init(int width, int height, Handler handler) {
        
        //Scene2D requires an associated InputHandler.
        if(!(handler instanceof InputHandler))
            throw new IllegalStateException("Missing InputHandler.");
        
        //Initialize shader.
        shader.init();
        
        //Initialize background.
        background.setSize(width, height);
        tiles.add(background);
        
        //Resize background upon window resize.
        InputHandler input = (InputHandler) handler;
        input.getHandler().register(WindowResizeEvent.class,
            e -> background.setSize(e.WIDTH, e.HEIGHT).update());
    }

    @Override
    public void render(int width, int height) {
        shader.render(tiles, width, height);
    }

    @Override
    public void destroy() {
        shader.destroy();
    }
    
    /**
     * Add a new tile to the scene.
     * @param tile to add.
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
    }
    
    /**
     * Remove a tile from the scene.
     * @param tile to remove.
     */
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }
    
    /** @return the background tile of the scene. */
    public FrameLayout getBackground() { return background; }
}