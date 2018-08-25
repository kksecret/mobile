package com.example.gameproject;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static java.lang.Math.cos;
import static java.lang.Math.sin;


public class Ball {

    private final int vertexCount = 30;
    float radius = 0.02f;
    float center_x = 0.0f;
    float center_y = -0.57f;

    final float basicVelocity = 0.015f;

    private float velocityX;
    private float velocityY;

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

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float ballCoords[];

    // Outer vertices of the circle
    private final int outerVertexCount = vertexCount-1;
    final float PI = (float)(Math.PI);

    private final int mProgram;
    private final float SCALE = 1.5f;

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.85547f, 0.19531f, 0.21094f, 1.0f };

    public Ball() {

        resetVelocity();

        ballCoords = new float[vertexCount * 3];

        for(int i = 0; i < outerVertexCount; i++){
            float percent = (i / (float) (outerVertexCount-1));
            float rad = percent * 2*PI;

            //Vertex position
            float outer_x = center_x + radius * 1.8f * (float)(cos(rad));
            float outer_y = center_y + radius * (float)(sin(rad));

            ballCoords[i * 3] = outer_x;
            ballCoords[i * 3 + 1] = outer_y;
            ballCoords[i * 3 + 2] = 0.0f;
        }

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                ballCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(ballCoords);
        vertexBuffer.position(0);

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

        // Draw circle as a filled shape
        glDrawArrays(GL_TRIANGLE_FAN, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public void setCenter(float x, float y)
    {
        center_x = x;
        center_y = y;

        for(int i = 0; i < outerVertexCount; i++){
            float percent = (i / (float) (outerVertexCount-1));
            float rad = percent * 2*PI;

            //Vertex position
            float outer_x = center_x + radius * 1.8f * (float)(cos(rad));
            float outer_y = center_y + radius * (float)(sin(rad));

            ballCoords[i * 3] = outer_x;
            ballCoords[i * 3 + 1] = outer_y;
            ballCoords[i * 3 + 2] = 0.0f;
        }

        // actualize vertex byte buffer for shape coordinates
        ByteBuffer bb2 = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                ballCoords.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        vertexBuffer.clear(); // clear
        vertexBuffer = bb2.asFloatBuffer();
        vertexBuffer.put(ballCoords);
        vertexBuffer.position(0);

    }

    public void resetVelocity()
    {
        float additionalVelocity;
        float newVelocity;

        Random randV = new Random();
        additionalVelocity = randV.nextFloat() * 0.01f;

        newVelocity = basicVelocity + additionalVelocity;

        Random randA = new Random();
        float newAngle = (float) (randA.nextFloat() * Math.PI/4 + Math.PI/8);

        int sign = 1;
        Random randVXsign = new Random();
        if(randVXsign.nextBoolean()) sign = -1;

        velocityX = sign * (float) (Math.sin(newAngle)) * newVelocity;
        velocityY = (float) (Math.cos(newAngle)) * newVelocity;
    }

    public float getCenterX()
    {
        return center_x;
    }

    public float getCenterY()
    {
        return center_y;
    }

    public float getVelocityX()
    {
        return velocityX;
    }

    public float getVelocityY()
    {
        return velocityY;
    }

    public void wallCollisionReact(CollisionInfo CI)
    {
        if(CI.checkCollisionDetected())
        {
            if(CI.checkHorizontalEdge())
            {
                velocityY = -velocityY;
            }
            else
            {
                velocityX = -velocityX;
            }
        }
    }

    public void platformCollisionReact(CollisionPlatformInfo CI)
    {
        float additionalVelocity;
        float newVelocity;

        if(CI.checkCollisionDetected())
        {
            Random randV = new Random();
            additionalVelocity = randV.nextFloat() * 0.01f;

            newVelocity = basicVelocity + additionalVelocity;

            Random randA = new Random();
            float newAngle = (float) (randA.nextFloat() * Math.PI/4 + Math.PI/8);

            float signVelocityX = Math.signum(velocityX);

            velocityX = signVelocityX * (float) (Math.sin(newAngle)) * newVelocity;
            velocityY = (float) (Math.cos(newAngle)) * newVelocity;
        }
    }

}
