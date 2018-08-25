package com.example.gameproject;

import java.util.Vector;


public class Blocks {
    private final int COL_N = 5;
    private final int ROW_N = 6;

    private final float TOP_MARGIN = 0.2f;
    private final float LEFT_MARGIN = 0.1f;

    private final float X_SPACE = 0.08f;
    private final float Y_SPACE = 0.04f;

    private final float BLOCK_WIDTH = 0.3f;
    private final float BLOCK_HEIGHT = 0.051f;

    private final float COLOR_BLOCK_R = 0.28125f;
    private final float COLOR_BLOCK_G = 0.51953f;
    private final float COLOR_BLOCK_B = 0.92578f;

    private Vector<Block> blocks = new Vector(COL_N * ROW_N);

    public Blocks()
    {
        for(int i = 0; i < ROW_N; i++)
        {
            for (int j = 0; j < COL_N; j++)
            {
                blocks.addElement(new Block(-1.0f + LEFT_MARGIN + j * (X_SPACE + BLOCK_WIDTH), 1.0f - TOP_MARGIN - i * (Y_SPACE + BLOCK_HEIGHT),
                        BLOCK_WIDTH, BLOCK_HEIGHT, COLOR_BLOCK_R, COLOR_BLOCK_G, COLOR_BLOCK_B));
            }
        }

    }


    public void draw()
    {
        for(int i = 0; i < ROW_N; i++)
        {
            for (int j = 0; j < COL_N; j++)
            {
                blocks.get(i * COL_N + j).draw();
            }
        }
    }

    public CollisionInfo detectCollision(float ballCenterX, float ballCenterY, float ballVelocityX, float ballVelocityY)
    {
        CollisionInfo CIGeneral = new CollisionInfo(false,false);
        for(int i = 0; i < COL_N * ROW_N; i++)
        {
            CollisionInfo CI = blocks.get(i).detectCollision(ballCenterX, ballCenterY, ballVelocityX, ballVelocityY);

            if(CI.checkCollisionDetected())
            {
                CIGeneral = CI;
            }
        }

        return CIGeneral;
    }

    public boolean noDisplayedBlocks()
    {
        int numberOfDisplayedBlocks = 0;

        for(int i = 0; i < ROW_N; i++)
        {
            for (int j = 0; j < COL_N; j++)
            {
                if(blocks.get(i * COL_N + j).checkDisplay())
                    numberOfDisplayedBlocks++;
            }
        }

        return (numberOfDisplayedBlocks <= 0);
    }
}
