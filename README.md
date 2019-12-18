# swag-ui
A basic UI library for Java games.

#### Example
```java
Scene2D scene = new Scene2D();
InputHandler input = new InputHandler();

new Tile(scene, 0, 0, 100, 100)
    .setDepth(1);

new Tile(scene, 50, 50, 100, 100)
    .setColour(Colour.LYNX_WHITE)
    .setAngle(10);

new Button(scene, input, -250, 250, 200, 50) {
    @Override
    protected void onLeftClick() {
        System.out.println("Hello, World!");
    }
};

new Window(1280, 960, "Game", scene, input);
```
