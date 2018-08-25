package com.example.gameproject;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public class Rectangle {
    protected float topLeftCornerX;
    protected float topLeftCornerY;
    protected float width;
    protected float height;
    float color[];

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float rectangleCoords[]; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int mProgram;


    public Rectangle(float tLCx, float tLCy, float w, float h, float r, float g, float b) {

        // Set color with red, green, blue and alpha (opacity) values
        color = new float[]{r, g, b, 1.0f };

        topLeftCornerX = tLCx;
        topLeftCornerY = tLCy;
        width = w;
        height = h;
        rectangleCoords = new float[]{
                topLeftCornerX,  topLeftCornerY, 0.0f,   // top left
                topLeftCornerX, topLeftCornerY - height, 0.0f,   // bottom left
                topLeftCornerX + width, topLeftCornerY - height, 0.0f,   // bottom right
                topLeftCornerX + width,  topLeftCornerY, 0.0f }; // top right
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                rectangleCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(rectangleCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = 4;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw() {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void setX(float x)
    {
        topLeftCornerX = x;

        rectangleCoords[0] = topLeftCornerX;
        rectangleCoords[1] = topLeftCornerY;

        rectangleCoords[3] = topLeftCornerX;
        rectangleCoords[4] = topLeftCornerY - height;

        rectangleCoords[6] = topLeftCornerX + width;
        rectangleCoords[7] = topLeftCornerY - height;

        rectangleCoords[9] = topLeftCornerX + width;
        rectangleCoords[10] = topLeftCornerY;

        // actualize vertex byte buffer for shape coordinates
        ByteBuffer bb2 = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                rectangleCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        vertexBuffer.clear(); // clear
        vertexBuffer = bb2.asFloatBuffer();
        vertexBuffer.put(rectangleCoords);
        vertexBuffer.position(0);

        // actualize byte buffer for the draw list
        ByteBuffer dlb2 = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb2.order(ByteOrder.nativeOrder());
        drawListBuffer.clear();
        drawListBuffer = dlb2.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

    }

}

