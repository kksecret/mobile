package com.example.gameproject;


public class CollisionInfo extends  CollisionPlatformInfo{
    private boolean horizontalEdge; // value false means vertical edge

    public CollisionInfo(boolean cD, boolean hE)
    {
        super(cD);
        horizontalEdge = hE;
    }

    public boolean checkHorizontalEdge()
    {
        return horizontalEdge;
    }
}
