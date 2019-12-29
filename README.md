# SwagUI
A graphics library for 2D Java games using OpenGL/GLFW/LWJGL.

#### Installation
Simply include 'SwagUI vX.X.jar' in your project.

#### Example usage
Example 1
```java
Scene2D scene = new Scene2D();
InputHandler input = new InputHandler();
Window window = new Window(1280, 960, "Example 1", scene, input);

scene.getRoot().addTile(new Tile(0, 0, 100, 100)
    .setDepth(1));

scene.getRoot().addTile(new Tile(50, 50, 100, 100)
    .setColour(Colour.LYNX_WHITE)
    .setAngle(10));

scene.getRoot().addTile(new Button(input, -250, 250, 200, 50) {
    @Override
    protected void onLeftClick() {
        System.out.println("Hello, World!");
    }
});

window.open();
```
Example 2
```java
Scene2D scene = new Scene2D();
InputHandler input = new InputHandler();
new Window(1280, 960, "Example 2", scene, input);

scene.getBackground().addTile(new HorizontalList(
    new Tile()
        .setColour(Colour.LYNX_WHITE)
        .setFill(Fill.FILL_PARENT),
    new Tile()
        .setColour(Colour.NAVAL)
        .setFill(Fill.FILL_PARENT),
    new Tile()
        .setColour(Colour.ELECTROMAGNETIC)
        .setFill(Fill.FILL_PARENT)));

window.open();
```
#### Notes
This is by no means a complete UI suite, but rather an ongoing project where I will add new features as I need them.
That being said, should anyone request a new feature, I'd be happy to oblige, provided it isn't stupid.
(Alternatively, and dare I say even better, you could implement it yourself.)
