package com.example.gameproject;


public class CollisionPlatformInfo{
    private boolean collisionDetected;

    public CollisionPlatformInfo(boolean cD)
    {
        collisionDetected = cD;
    }

    public boolean checkCollisionDetected()
    {
        return collisionDetected;
    }

}
