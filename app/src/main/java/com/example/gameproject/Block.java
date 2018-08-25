package com.example.gameproject;


public class Block extends Rectangle{
    private boolean display;

    public Block(float tLCx, float tLCy, float w, float h, float r, float g, float b)
    {
        super(tLCx, tLCy, w, h, r, g, b);
        display = true;
    }

    public void draw()
    {
        if(display)
            super.draw();
    }

    public CollisionInfo detectCollision(float ballCenterX, float ballCenterY, float ballVelocityX, float ballVelocityY)
    {
        if(display && (topLeftCornerX - 0.036f <= ballCenterX) && (ballCenterX <= topLeftCornerX + width + 0.036f) &&
                (topLeftCornerY + 0.02f >= ballCenterY) && (ballCenterY >= topLeftCornerY - height - 0.02f))
        {
            display = false;

            float previousBallCenterX = ballCenterX - ballVelocityX;
            float previousBallCenterY = ballCenterY - ballVelocityY;
            // vertical collisions
            if ((previousBallCenterX < ballCenterX) && (verticalLineCollision(topLeftCornerY - height - 0.02f, topLeftCornerY + 0.02f, topLeftCornerX,
                    previousBallCenterX, previousBallCenterY, ballCenterX, ballCenterY)))
            {
                return(new CollisionInfo(true, false));
            }
            if ((previousBallCenterX > ballCenterX) && (verticalLineCollision(topLeftCornerY - height - 0.02f, topLeftCornerY + 0.02f, topLeftCornerX + width,
                    previousBallCenterX, previousBallCenterY, ballCenterX, ballCenterY)))
            {
                return(new CollisionInfo(true, false));
            }
            // horizontal collisions
            if ((previousBallCenterY > ballCenterY) && (horizontalLineCollision(topLeftCornerX - 0.036f, topLeftCornerX + width + 0.036f, topLeftCornerY + 0.02f,
                    previousBallCenterX, previousBallCenterY, ballCenterX, ballCenterY)))
            {
                return(new CollisionInfo(true, true));
            }
            if ((previousBallCenterY < ballCenterY) && (horizontalLineCollision(topLeftCornerX - 0.036f, topLeftCornerX + width + 0.036f, topLeftCornerY - height - 0.02f,
                    previousBallCenterX, previousBallCenterY, ballCenterX, ballCenterY)))
            {
                return(new CollisionInfo(true, true));
            }

        }

        // else
        return(new CollisionInfo(false, false));
    }

    public boolean horizontalLineCollision(float horLineBeginX,
                                           float horLineEndX, float horLineY,
                                           float pointOutX, float pointOutY,
                                           float pointInX, float pointInY)
    {
        if(pointOutX == pointInX) return false;

        float x = (horLineY - pointOutY) * (pointInX - pointOutX) / (pointInY - pointOutY) + pointOutX;

        if((horLineBeginX <= x) && (x <= horLineEndX)) return true;

        return false;
    }

    public boolean verticalLineCollision(float verLineBeginY,
                                           float verLineEndY, float verLineX,
                                           float pointOutX, float pointOutY,
                                           float pointInX, float pointInY)
    {
        return horizontalLineCollision(verLineBeginY, verLineEndY, verLineX, pointOutY, pointOutX, pointInY, pointInX);
    }

    public boolean checkDisplay()
    {
        return display;
    }

}
