package com.example.gameproject;


public class Platform extends Rectangle {


    public Platform(float tLCx, float tLCy, float w, float h, float r, float g, float b)
    {
        super(tLCx, tLCy, w, h, r, g, b);
    }

    public CollisionPlatformInfo detectCollision(float ballCenterX, float ballCenterY)
    {
        float platformX = topLeftCornerX;
        float platformY = topLeftCornerY;
        final float platformWidth = 0.6f;

        if((platformX - 0.02f <= ballCenterX) && (ballCenterX <= platformX + platformWidth + 0.02f))
        {
            if(ballCenterY - 0.02f <= platformY)
                return(new CollisionPlatformInfo(true));
        }

        // else
        return(new CollisionPlatformInfo(false));
    }

}
