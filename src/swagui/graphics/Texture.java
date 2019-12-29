package swagui.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * Texture for rendering.
 * @author Alec Dorrington
 */
public class Texture {
    
    /** Name of texture file. */
    private String fileName;
    
    /** ID of texture. */
    private int textureId = -1;
    
    /** Whether the texture is fully opaque. */
    private boolean opaque = false;
    
    /**
     * Load a new texture from file.
     * @param fileName of image.
     */
    public Texture(String fileName) {
        this.fileName = fileName;
    }
    
    /**
     * @return ID of this texture.
     */
    public int getTextureId() {
        if(textureId == -1) textureId = loadPng(fileName);
        return textureId;
    }
    
    /**
     * @return Whether the texture is fully opaque.
     */
    public boolean isOpaque() { return opaque; }
    
    /**
     * Load a texture from PNG file into byte buffer then OpenGL.
     * @param fileName the image file to load.
     * @return the ID of the texture.
     */
    private int loadPng(String fileName) {
        
        //Load image from file.
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(fileName));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        //Copy pixels into array.
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(),
                pixels, 0, image.getWidth());
        
        ByteBuffer buffer = BufferUtils.createByteBuffer(
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
        return loadTexture(image, buffer);
    }
    
    /**
     * Load a texture from byte buffer into OpenGL.
     * @param image which was loaded.
     * @param buffer of pixels values in image.
     * @return the ID of the texture.
     */
    private int loadTexture(BufferedImage image, ByteBuffer buffer) {
        
        //Create new texture.
        int textureId = glGenTextures();
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
        
        return textureId;
    }
}