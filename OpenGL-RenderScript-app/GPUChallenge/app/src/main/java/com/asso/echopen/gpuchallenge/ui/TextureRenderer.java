package com.asso.echopen.gpuchallenge.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.asso.echopen.gpuchallenge.model.BitmapDisplayer;
import com.asso.echopen.gpuchallenge.model.BitmapDisplayerFactory;
import com.asso.echopen.gpuchallenge.model.FileTask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by mehdibenchoufi on 13/09/16.
 */

public class TextureRenderer implements GLSurfaceView.Renderer
{

    // Handle to a program object
    private int programObject;

    // Attribute locations
    private int positionLoc;
    private int texCoordLoc;

    // Sampler location
    private int baseMapLoc;
    private int lightMapLoc;

    // Texture handle
    private int baseMapTexId;
    private int lightMapTexId;

    // Additional member variables
    private int width;
    private int height;
    private FloatBuffer vertices;
    private ShortBuffer indices;
    private Context context;

    // Randomize
    private Random random = new Random();

    private final float[] mVerticesData =
            {
                    -0.5f, 0.5f, 0.0f, // Position 0
                    0.0f, 0.0f, // TexCoord 0
                    -0.5f, -0.5f, 0.0f, // Position 1
                    0.0f, 1.0f, // TexCoord 1
                    0.5f, -0.5f, 0.0f, // Position 2
                    1.0f, 1.0f, // TexCoord 2
                    0.5f, 0.5f, 0.0f, // Position 3
                    1.0f, 0.0f // TexCoord 3
            };

    private final short[] mIndicesData =
            {
                    0, 1, 2, 0, 2, 3
            };

    public static ByteBuffer buffer = ByteBuffer.allocateDirect(512*512*3);

    private MainActionController mainActionController;

    public TextureRenderer(Context context)
    {
        context = context;
        vertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertices.put(mVerticesData).position(0);
        indices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        indices.put(mIndicesData).position(0);
    }

    ///
    //  Load texture from resource
    //
    private int loadTexture()
    {
        int[] textureId = new int[1];

        byte[] data = new byte[512*512*3];
        for ( int y = 0; y < 512; y++ )
            for ( int x = 0; x < 512; x++ )
            {
                int pixel = 50;
                data[(y * 512 + x) * 3 + 0] = (byte)((pixel >> 0) & 0xFF);
                data[(y * 512 + x) * 3 + 1] = (byte)((pixel >> 0) & 0xFF);
                data[(y * 512 + x) * 3 + 2] = (byte)((pixel >> 0) & 0xFF);
            }
        buffer.clear();
        buffer.put(data);
        buffer.position(0);

        GLES20.glGenTextures(1, textureId, 0);
        GLES20.glBindTexture ( GLES20.GL_TEXTURE_2D, textureId[0] );

        GLES20.glTexImage2D ( GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, 512, 512, 0,
                GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, buffer );

        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri (GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        return textureId[0];
    }

    public void onDrawFrame(GL10 glUnused)
    {
        // Set the viewport
        GLES20.glViewport(0, 0, width, height);

        // Clear the color buffer
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Use the program object
        GLES20.glUseProgram(programObject);

        // Load the vertex position
        vertices.position(0);
        GLES20.glVertexAttribPointer(positionLoc, 3, GLES20.GL_FLOAT,
                false,
                5 * 4, vertices);
        // Load the texture coordinate
        vertices.position(3);
        GLES20.glVertexAttribPointer(texCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                5 * 4,
                vertices);

        GLES20.glEnableVertexAttribArray(positionLoc);
        GLES20.glEnableVertexAttribArray(texCoordLoc);

        byte[] data = new byte[512*512*3];

        int[] scanned_data = FileTask.getScanned_array();

        for ( int y = 0; y < 512; y++ )
            for ( int x = 0; x < 512; x++ )
            {
                int pixel = random.nextInt(255);
                data[(y * 512 + x) * 3 + 0] = (byte)((scanned_data[y * 512 + x] >> 0) & 0xFF);
                data[(y * 512 + x) * 3 + 1] = (byte)((scanned_data[y * 512 + x] >> 0) & 0xFF);
                data[(y * 512 + x) * 3 + 2] = (byte)((scanned_data[y * 512 + x] >> 0) & 0xFF);
            }
        ByteBuffer bufer = ByteBuffer.allocateDirect(512*512*3);
        bufer.clear();
        bufer.put(data);
        bufer.position(0);

        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, 512, 512,
                GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, bufer);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_SHORT, indices);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, javax.microedition.khronos.egl.EGLConfig eglConfig) {
        String vShaderStr =
                "attribute vec4 a_position;   \n" +
                        "attribute vec2 a_texCoord;   \n" +
                        "varying vec2 v_texCoord;     \n" +
                        "void main()                  \n" +
                        "{                            \n" +
                        "   gl_Position = a_position; \n" +
                        "   v_texCoord = a_texCoord;  \n" +
                        "}                            \n";

        String fShaderStr =
                "precision mediump float;                            \n" +
                        "varying vec2 v_texCoord;                            \n" +
                        "uniform sampler2D s_baseMap;                        \n" +
                        "uniform sampler2D s_lightMap;                       \n" +
                        "void main()                                         \n" +
                        "{                                                   \n" +
                        "  vec4 baseColor;                                   \n" +
                        "  vec4 lightColor;                                  \n" +
                        "                                                    \n" +
                        "  baseColor = texture2D( s_baseMap, v_texCoord );   \n" +
                        "  lightColor = texture2D( s_lightMap, v_texCoord ); \n" +
                        "  gl_FragColor = baseColor * (lightColor + 0.25);   \n" +
                        "}                                                   \n";

        // Load the shaders and get a linked program object
        programObject = ESShader.loadProgram(vShaderStr, fShaderStr);

        // Get the attribute locations
        positionLoc = GLES20.glGetAttribLocation(programObject, "a_position");
        texCoordLoc = GLES20.glGetAttribLocation(programObject, "a_texCoord");

        baseMapTexId = loadTexture();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    ///
    // Handle surface changes
    //
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        this.width = width;
        this.height = height;
    }
}
