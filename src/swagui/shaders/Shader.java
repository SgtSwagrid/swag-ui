package swagui.shaders;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swagui.graphics.Colour;
import swagui.math.Matrix4;

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
    private String vertexShaderSrc, fragmentShaderSrc;
    
    /** Table of uniform variable locations. */
    private Map<String, Integer> uniforms = new HashMap<>();
    
    /**
     * Creates a new shader from the given source files.
     * @param vertexShaderFile the source file for the vertex shader.
     * @param fragmentShaderFile the source file for the fragment shader.
     */
    protected Shader(String vertexShaderSrc, String fragmentShaderSrc) {
        this.vertexShaderSrc = vertexShaderSrc;
        this.fragmentShaderSrc = fragmentShaderSrc;
    }
    
    /**
     * Initialize shader prior to rendering.
     */
    public void init() {
        
        //Compile shader source.
        vertexShaderId = compile(vertexShaderSrc, GL_VERTEX_SHADER);
        fragmentShaderId = compile(fragmentShaderSrc, GL_FRAGMENT_SHADER);
        
        //Create shader program and attach shaders.
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexShaderId);
        glAttachShader(shaderProgramId, fragmentShaderId);
        
        //Bind VBOs.
        onBind();
        
        //Link and validate shader program.
        glLinkProgram(shaderProgramId);
        glValidateProgram(shaderProgramId);
        
        //Initialize shader.
        glUseProgram(shaderProgramId);
        onInit();
        glUseProgram(0);
    }
    
    /**
     * Destroy shader upon completion.
     */
    public void destroy() {
        
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
     * @return the ID of this shader program.
     */
    protected int getShaderProgramId() { return shaderProgramId; }
    
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
    
    /**
     * Load uniform to GPU.
     * @param name of uniform variable.
     * @param colour to be loaded.
     */
    protected void setUniform(String name, Colour colour) {
        glUniform4f(locationOf(name), colour.R, colour.G, colour.B, colour.A);
    }
    
    /**
     * Load uniform to GPU.
     * @param name of uniform variable.
     * @param colours to be loaded.
     */
    protected void setUniform(String name, List<Colour> colours) {
        for(int i = 0; i < colours.size(); i++) {
            setUniform(name+"["+i+"]", colours.get(i));
        }
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
    private int compile(String shaderSrc, int shaderType) {
        
        //Read source file.
        String src = loadResource(shaderSrc);
        
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
     * Read entire contents of file/resource to string.
     * @param path the relative path of the file.
     * @return the contents of the file.
     */
    private String loadResource(String name) {
        
        InputStream in = getClass().getResourceAsStream(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.lines().reduce("", (l1, l2) -> l1 + "\n" + l2);
    }
    
    /**
     * Bind VBOs prior to initialization.
     */
    protected abstract void onBind();
    
    /**
     * Initialize this shader.
     */
    protected abstract void onInit();
}