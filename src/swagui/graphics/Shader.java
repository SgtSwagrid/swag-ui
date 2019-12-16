package swagui.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * An OpenGL shader program.
 * @author Alec Dorrington
 */
public abstract class Shader {
    
    /**
     * The IDs of the shaders and shader program.
     */
    private int shaderProgramId, vertexShaderId, fragmentShaderId;
    
    /**
     * The source files of the shaders.
     */
    private String vertexShaderFile, fragmentShaderFile;
    
    /** Table of uniform variable locations. */
    private Map<String, Integer> uniforms = new HashMap<>();
    
    /**
     * Creates a new shader from the given source files.
     * @param vertexShaderFile the source file for the vertex shader.
     * @param fragmentShaderFile the source file for the fragment shader.
     */
    protected Shader(String vertexShaderFile, String fragmentShaderFile) {
        this.vertexShaderFile = vertexShaderFile;
        this.fragmentShaderFile = fragmentShaderFile;
    }
    
    /**
     * Initialize shader prior to rendering.
     */
    public void doInit() {
        
        //Compile shader source.
        vertexShaderId = compile(vertexShaderFile, GL_VERTEX_SHADER);
        fragmentShaderId = compile(fragmentShaderFile, GL_FRAGMENT_SHADER);
        
        //Create shader program and attach shaders.
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexShaderId);
        glAttachShader(shaderProgramId, fragmentShaderId);
        
        //Bind VBOs.
        bind();
        
        //Link and validate shader program.
        glLinkProgram(shaderProgramId);
        glValidateProgram(shaderProgramId);
        
        //Initialize shader.
        glUseProgram(shaderProgramId);
        init();
        glUseProgram(0);
    }
    
    /**
     * Render a frame.
     * @param width of the window (pixels).
     * @param height of the window (pixels).
     */
    public void doRender(int width, int height) {
        
        glUseProgram(shaderProgramId);
        render(width, height);
        glUseProgram(0);
    }
    
    /**
     * Destroy shader upon completion.
     */
    public void doDestroy() {
        
        //Detach shaders.
        glUseProgram(0);
        glDetachShader(shaderProgramId, vertexShaderId);
        glDetachShader(shaderProgramId, fragmentShaderId);
        
        //Delete shaders and shader program.
        glDeleteShader(vertexShaderId);
        glDeleteShader(vertexShaderId);
        glDeleteProgram(shaderProgramId);
    }
    
    /**
     * Load uniform to GPU.
     * @param name of uniform variable.
     * @param matrix value to be loaded.
     */
    protected void setUniform(String name, Matrix4 matrix) {
        glUniformMatrix4fv(locationOf(name), true, matrix.asFloatBuffer());
    }
    
    /**
     * Load uniform to GPU.
     * @param name of uniform variable.
     * @param value to be loaded.
     */
    protected void setUniform(String name, boolean value) {
        glUniform1f(locationOf(name), value ? 1 : 0);
    }
    
    protected void setUniform(String name, Colour colour) {
        glUniform4f(locationOf(name), colour.R, colour.G, colour.B, colour.A);
    }
    
    /**
     * Finds the location of the uniform variable of the given name.
     * @param name of the uniform variable.
     * @return the location of the variable.
     */
    private int locationOf(String name) {
        uniforms.putIfAbsent(name,
                glGetUniformLocation(shaderProgramId, name));
        return uniforms.get(name);
    }
    
    /**
     * @return the ID of this shader program.
     */
    protected int getShaderID() { return shaderProgramId; }
    
    /**
     * @param shaderFile the name of the shader's file.
     * @param shaderType the type of shader (vertex/fragment).
     * @return the ID of the compiled shader.
     */
    private int compile(String shaderFile, int shaderType) {
        
        //Read source file.
        String src = readFile(shaderFile);
        
        //Compile shader source.
        int shaderId = glCreateShader(shaderType);
        glShaderSource(shaderId, src);
        glCompileShader(shaderId);
        
        //Check for errors.
        if(glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(shaderId, 500));
            System.exit(0);
        }
        return shaderId;
    }
    
    /**
     * Read entire contents of file to string.
     * @param path the name of the file.
     * @return the contents of the file.
     */
    private String readFile(String path) {
        
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    
    /**
     * Bind VBOs prior to initialization.
     */
    protected abstract void bind();
    
    /**
     * Initialize this shader.
     */
    protected abstract void init();
    
    /**
     * Render a frame.
     * @param width of the window (pixels).
     * @param height of the window (pixels).
     */
    protected abstract void render(int width, int height);
}