package swagui.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

/**
 * A VAO-based mesh.
 * @author Alec Dorrington
 */
public class Mesh {
    
    /** Set of VAO IDs, and associated VBO IDs for each. */
    private static Map<Integer, List<Integer>> vaos = new HashMap<>();
    
    /** The ID of the VAO of the mesh. */
    private int vaoId;
    
    /** The number of vertices in the mesh. */
    private int numVertices;
    
    /**
     * Create a new mesh.
     * @param vertices array of 2D vertices.
     * @param texmap array of 2D texture coordinates.
     */
    public Mesh(float[] vertices, float[] texmap) {
        vaoId = createVao(vertices, texmap);
        numVertices = vertices.length;
    }
    
    /**
     * @return the ID of the VAO of the mesh.
     */
    public int getVaoId() { return vaoId; }
    
    /**
     * @return the number of vertices in the mesh.
     */
    public int getNumVertices() { return numVertices; }
    
    /**
     * Create a new VAO.
     * @param vertices list of vertices.
     * @param textures list of texture coordinates.
     * @return VAO ID.
     */
    private int createVao(float[] vertices, float[] textures) {
        
        //Create new VAO.
        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        
        //Create VBOs for vertices and texcoords.
        List<Integer> vbos = new ArrayList<>(2);
        vbos.add(createVbo(0, vertices, 2));
        vbos.add(createVbo(1, textures, 2));
        
        //Save, unbind and return.
        vaos.put(vaoId, vbos);
        glBindVertexArray(0);
        return vaoId;
    }
    
    /**
     * Create a new VBO.
     * @param attribId ID of VBO.
     * @param data to load into VBO.
     * @param dim dimensionality of data.
     * @return VBO ID.
     */
    private int createVbo(int attribId, float[] data, int dim) {
        
        //Create new VBO.
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        
        //Put data in buffer.
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        
        //Load data into VBO.
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attribId, dim, GL_FLOAT, false, 0, 0);
        
        //Unbind and return.
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        return vboId;
    }
    
    public static void unloadAll() {
        
        //Delete each VAO.
        for(int vaoId : vaos.keySet()) {
            glDeleteVertexArrays(vaoId);
            //Delete each VBO.
            for(int vboId : vaos.get(vaoId)) {
                glDeleteBuffers(vboId);
            }
        }
    }
}