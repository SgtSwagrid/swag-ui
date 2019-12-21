package swagui;

import swagui.graphics.Colour;
import swagui.input.InputHandler;
import swagui.tiles.Scene2D;
import swagui.tiles.Tile;
import swagui.tiles.Tile.Alignment;
import swagui.window.Window;

public class Test {
    
    public static void main(String[] args) {
        
        Scene2D scene = new Scene2D();
        InputHandler input = new InputHandler();
        
        scene.getRoot().addTile(
            new Tile()
                .setSize(200, 200));
        
        scene.getBackground().addTile(
            new Tile()
                .setAlignment(Alignment.TOP_LEFT)
                .setColour(Colour.LYNX_WHITE));
        
        new Window(640, 480, "Test", scene, input);
    }
}