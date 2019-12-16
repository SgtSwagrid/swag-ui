package swagui.math;

import static java.lang.Math.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * 4x4 square matrix.
 * @author Alec Dorrington
 */
public class Matrix4 {
    
    /** Array of values in matrix. */
    private float[][] matrix = new float[4][4];
    
    /**
     * Creates a new matrix from the given 2D array.
     * @param matrix the 4x4 float array from which to create the matrix.
     */
    public Matrix4(float[][] matrix) {
        this.matrix = copy(matrix);
    }
    
    /**
     * Right-multiply by the given matrix.
     * @param mat the 4x4 matrix by which to multiply.
     * @return the product matrix.
     */
    public Matrix4 mul(Matrix4 mat) {
        
        float[][] product = new float[4][4];
        
        //For each entry in product matrix.
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                //Get dot product of row/column of multiplicands.
                for(int k = 0; k < 4; k++) {
                    product[i][j] += matrix[i][k] * mat.matrix[k][j];
                }
            }
        }
        return new Matrix4(product);
    }
    
    /**
     * @return this matrix, as a 2D float array.
     */
    public float[][] asArray() { return matrix; }
    
    /**
     * @return this matrix, as a float buffer.
     */
    public FloatBuffer asFloatBuffer() {
        
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        for(float[] row : matrix) {
            buffer.put(row);
        }
        buffer.flip();
        return buffer;
    }
    
    /** 
     * @param array to copy.
     * @return copy of the array.
     */
    private float[][] copy(float[][] array) {
        
        float[][] newArray = new float[array.length][];
        for(int i = 0; i < array.length; i++) {
            newArray[i] = array[i].clone();
        }
        return newArray;
    }
    
    /** The 4x4 identity matrix. */
    public static final Matrix4 IDENTITY = new Matrix4(new float[][] {
        {1.0F, 0.0F, 0.0F, 0.0F},
        {0.0F, 1.0F, 0.0F, 0.0F},
        {0.0F, 0.0F, 1.0F, 0.0F},
        {0.0F, 0.0F, 0.0F, 1.0F}
    });
    
    /**
     * Creates a translation matrix.
     * @param tx x-translation.
     * @param ty y-translation.
     * @param tz z-translation.
     * @return the translation matrix.
     */
    public static Matrix4 getTranslationMatrix(float tx, float ty, float tz) {
        return new Matrix4(new float[][] {
            {1.0F, 0.0F, 0.0F, tx},
            {0.0F, 1.0F, 0.0F, ty},
            {0.0F, 0.0F, 1.0F, tz},
            {0.0F, 0.0F, 0.0F, 1.0F}
        });
    }
    
    /**
     * Creates a 2D rotation matrix.
     * @param angle (degrees, anti-clockwise about z-axis).
     * @return the rotation matrix.
     */
    public static Matrix4 getRotationMatrix(float angle) {
        
        float sin = (float)sin(toRadians(angle));
        float cos = (float)cos(toRadians(angle));
        
        return new Matrix4(new float[][] {
            {cos, -sin, 0.0F, 0.0F},
            {sin, cos, 0.0F, 0.0F},
            {0.0F, 0.0F, 1.0F, 0.0F},
            {0.0F, 0.0F, 0.0F, 1.0F}
        });
    }
    
    /**
     * Creates a scale matrix.
     * @param sx x-scale.
     * @param sy y-scale.
     * @param sz z-scale.
     * @return the scale matrix.
     */
    public static Matrix4 getScaleMatrix(float sx, float sy, float sz) {
        return new Matrix4(new float[][] {
            {sx, 0.0F, 0.0F, 0.0F},
            {0.0F, sy, 0.0F, 0.0F},
            {0.0F, 0.0F, sz, 0.0F},
            {0.0F, 0.0F, 0.0F, 1.0F}
        });
    }
}