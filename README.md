# SwagUI
A graphics library for 2D Java games.

#### Example usage
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

#### Notes
This is by no means a complete UI suite, but rather an ongoing project where I will add new features as I need them.
That being said, should anyone request a new feature, I'd be happy to oblige, provided it isn't stupid.
(Alternatively, and dare I say even better, you could implement it yourself.)

#### Required libraries
lwjgl-3.x.x
* lwjgl
* lwjgl-opengl
* lwjgl-glfw

(All included in /lib.)
