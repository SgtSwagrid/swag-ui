package swagui.tiles;

import java.util.SortedSet;
import java.util.TreeSet;

import swagui.shaders.TileShader;
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

    @Override
    public void init() {
        shader.init();
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
}