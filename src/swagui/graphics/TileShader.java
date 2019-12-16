package swagui.graphics;

import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Shader program for rendering quads.
 * @author Alec Dorrington
 */
public class TileShader extends Shader {
    
    /** Singleton tile shader instance. */
    public static final TileShader INSTANCE = new TileShader();
    
    /** Square tile mesh. */
    private Mesh tileMesh;
    
    /** List of tiles visible to shader. */
    private List<Tile> tiles = new LinkedList<>();
    
    /**
     * Create tile shader program from GLSL source.
     */
    private TileShader() {
        super("src/swagui/graphics/vertex.glsl",
              "src/swagui/graphics/fragment.glsl");
    }

    @Override
    protected void bind() {
        glBindAttribLocation(getShaderID(), 0, "vertex");
        glBindAttribLocation(getShaderID(), 1, "texmap");
    }

    @Override
    protected void render(int width, int height) {
        
        loadMesh(tileMesh);
        
        //Load view matrix to account for window size.
        setUniform("view", Matrix4.getScaleMatrix(
                2.0F/width, 2.0F/height, 1.0F));
        
        //Render each tile.
        tiles.forEach(this::renderTile);
        unloadMesh();
    }
    
    /**
     * Render a tile to the screen.
     * @param tile to render.
     */
    private void renderTile(Tile tile) {
        
        //Load texture.
        if(tile.getTexture().isPresent()) {
            loadTexture(tile.getTexture().get());
        }
        
        setUniform("transform", tile.getTransform());
        setUniform("colour", tile.getColour());
        setUniform("textured", tile.getTexture().isPresent());
        
        //Render tile.
        glDrawArrays(GL_TRIANGLES, 0, tileMesh.getNumVertices());
    }
    
    /**
     * Load a mesh to OpenGL.
     * @param mesh to load.
     */
    private void loadMesh(Mesh mesh) {
        
        //Load VAO.
        glBindVertexArray(mesh.getVaoId());
        
        //Load each VBO.
        for(int i = 0; i < 2; i++) {
            glEnableVertexAttribArray(i);
        }
    }
    
    /**
     * Unload current mesh.
     */
    private void unloadMesh() {
        
        //Unload each VBO.
        for(int i = 0; i < 2; i++) {
            glDisableVertexAttribArray(i);
        }
        
        //Unload VAO.
        glBindVertexArray(0);
    }
    
    /**
     * Load a texture to OpenGL.
     * @param texture to load.
     */
    private void loadTexture(Texture texture) {
        
        //Bind texture to TEXTURE0.
        glActiveTexture(GL_TEXTURE0);
        
        //Enable culling iff the texture is fully opaque.
        if(texture.isOpaque()) glEnable(GL_CULL_FACE);
        else glDisable(GL_CULL_FACE);
        
        //Load texture.
        glBindTexture(GL_TEXTURE_2D, texture.getTextureId());
    }
    
    /**
     * @param tile to make visible to shader.
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
        tiles.sort((t1, t2) -> t1.getDepth() - t2.getDepth());
    }
    
    /**
     * @param tile to hide from shader.
     */
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }
    
    @Override
    protected void init() {
        
        tileMesh = new Mesh(
            
            //Vertices.
            new float[] {-0.5F, -0.5F,
                         0.5F, 0.5F,
                         -0.5F, 0.5F,
                         0.5F, 0.5F,
                         -0.5F, -0.5F,
                         0.5F, -0.5F},
            
            //Texture coordinates.
            new float[] {0.0F, 1.0F,
                         1.0F, 0.0F,
                         0.0F, 0.0F,
                         1.0F, 0.0F,
                         0.0F, 1.0F,
                         1.0F, 1.0F});
    }
}