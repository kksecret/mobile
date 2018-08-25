package com.example.gameproject;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    private volatile float platformX = -0.3f;
    private final float platformY = -0.6f;

    private Platform platform;
    private Blocks blocks;
    private Ball ball;

    private Lifes mLifes;
    private Rectangle deathBackground;


    public MyGLRenderer(Context mC)
    {
        mContext = mC;
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        float greyFactor = 0.7f;
        GLES20.glClearColor(greyFactor, greyFactor, greyFactor, 1.0f);

        platform = new Platform(-0.3f, platformY, 0.6f, 0.02f, 0.95313f, 0.75781f, 0.05078f);
        blocks = new Blocks();
        ball = new Ball();
        mLifes = new Lifes();

        float transparenceFactor = 0.6f;
        deathBackground = new Rectangle(-1.0f + 0.005f, 1.0f - 0.005f, 1.99f, 1.99f,
                greyFactor + transparenceFactor * 0.85547f, greyFactor + transparenceFactor * 0.19531f, greyFactor + transparenceFactor * 0.21094f);

    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if(blocks.noDisplayedBlocks())
            openWinnerActivity();
        else
        {
            if(mLifes.checkNumberOfLifes() <= 0)
            {
                openGameOverActivity();
            }
            else
            {
                float old_x = ball.getCenterX();
                float old_y = ball.getCenterY();

                if (old_y < platformY -0.04f)
                {
                    // moving the ball
                    float new_x = old_x + ball.getVelocityX();
                    float new_y = old_y + ball.getVelocityY();
                    ball.setCenter(new_x, new_y);

                    if (-1.4f < new_y)
                    {
                        deathBackground.draw();

                        ball.draw();
                    }
                    else
                    {
                        mLifes.death();

                        ball.setCenter(0.0f, platformY + 0.03f);
                        ball.resetVelocity();
                    }

                }
                else
                {
                    float old_velocity_x = ball.getVelocityX();
                    float old_velocity_y = ball.getVelocityY();
                    // ball reacts to the platform collisions
                    ball.platformCollisionReact(platform.detectCollision(old_x, old_y));
                    // ball reacts to the block collisions
                    ball.wallCollisionReact(blocks.detectCollision(old_x, old_y, old_velocity_x, old_velocity_y));
                    // ball reacts to the frame collisions
                        // horizontal
                    ball.wallCollisionReact(detectFrameHorizontalCollision(old_y));
                        // vertical
                    ball.wallCollisionReact(detectFrameVerticalCollision(old_x));

                    // moving the ball
                    float new_x = old_x + ball.getVelocityX();
                    float new_y = old_y + ball.getVelocityY();
                    ball.setCenter(new_x, new_y);
                }

                platform.setX(platformX);

                platform.draw();

                blocks.draw();

                ball.draw();

                mLifes.draw();
            }
        }

    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void setPlatformX(float x)
    {
        platformX = x;
    }

    public CollisionInfo detectFrameHorizontalCollision(float ballCenterY)
    {
        final float TOP_MARGIN = 0.005f;
        final float BOTTOM_MARGIN = 0.005f;

        // collision with the top wall
        if(ballCenterY + 0.02f >= (1.0f - TOP_MARGIN))
            return(new CollisionInfo(true, true));

        /*// collision with the bottom wall (for testing)
        if(ballCenterY - 0.02f <= -(1.0f - BOTTOM_MARGIN))
            return(new CollisionInfo(true, true));*/

        // else
        return(new CollisionInfo(false, false));
    }

    public CollisionInfo detectFrameVerticalCollision(float ballCenterX)
    {
        final float LEFT_MARGIN = 0.005f;
        final float RIGHT_MARGIN = 0.005f;

        // collision with the left wall
        if(ballCenterX - 0.036f <= -(1.0f - LEFT_MARGIN))
            return(new CollisionInfo(true, false));

        // collision with the right wall
        if(ballCenterX + 0.036f >= (1.0f - RIGHT_MARGIN))
            return(new CollisionInfo(true, false));

        // else
        return(new CollisionInfo(false, false));
    }

    public void openWinnerActivity() {
        Intent intent = new Intent(mContext, WinnerActivity.class);
        mContext.startActivity(intent);
    }

    public void openGameOverActivity() {
        Intent intent = new Intent(mContext, GameOverActivity.class);
        mContext.startActivity(intent);
    }

}
