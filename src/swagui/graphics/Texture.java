package swagui.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * Texture for rendering.
 * @author Alec Dorrington
 */
public class Texture {
    
    /** Image file from which texture is sourced. */
    private BufferedImage image;
    
    /** Buffer containing image pixel data. */
    private ByteBuffer buffer;
    
    /** ID of texture. */
    private int textureId = -1;
    
    /** Whether the texture is fully opaque. */
    private boolean opaque = false;
    
    /**
     * Load a new texture from file.
     * @param fileName of image.
     */
    public Texture(String imageName) {
        loadPng(imageName);
    }
    
    /**
     * @return ID of this texture.
     */
    public int getTextureId() {
        if(textureId == -1) createTexture();
        return textureId;
    }
    
    /**
     * @return Whether the texture is fully opaque.
     */
    public boolean isOpaque() { return opaque; }
    
    /**
     * Load a texture from PNG file into byte buffer.
     * @param imageName name of image to load, from class loader or file.
     */
    private void loadPng(String imageName) {
        
        try {
            
            //Load resource from class loader or file.
            InputStream stream = getClass().getClassLoader()
                    .getResourceAsStream(imageName);
            if(stream == null) stream = new FileInputStream(imageName);
            image = ImageIO.read(stream);
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        //Copy pixels into array.
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                pixels, 0, image.getWidth());
        
        buffer = BufferUtils.createByteBuffer(
                image.getWidth() * image.getHeight() * 4);
        
        //Load array into buffer.
        for(int y = 0; y < image.getHeight(); y++) {
            for(int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[x + y * image.getWidth()];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); //Red
                buffer.put((byte) ((pixel >> 8) & 0xFF));  //Green
                buffer.put((byte) (pixel & 0xFF));         //Blue
                buffer.put((byte) (pixel >> 24));          //Alpha
            }
        }
        
        buffer.flip();
    }
    
    /**
     * Create an OpenGL texture from a byte buffer.
     * @param image which was loaded.
     * @param buffer of pixels values in image.
     * @return the ID of the texture.
     */
    private void createTexture() {
        
        //Create new texture.
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);
        
        //Clamping.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        
        //Filtering.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_NEAREST);
        
        //Load pixel buffer into texture.
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(),
                image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        
        //Generate mipmap pyramid.
        glGenerateMipmap(GL_TEXTURE_2D);
    }
}