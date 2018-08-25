package com.example.gameproject;


public class Lifes {

    private int numberOfLifes;

    final float HEIGHT = 0.10f;
    final float WIDTH = 0.06f;

    final float LEFT_MARGIN = 0.08f;
    final float BOTTOM_MARGIN = 0.04f;

    final float X_SPACE = 0.025f;

    Rectangle[] mLifes = new Rectangle[3];

    public Lifes()
    {
        numberOfLifes = 3;

        for (int i = 0;  i < 3; i++)
        {
            mLifes[i] = new Rectangle(-1.0f + LEFT_MARGIN + i * (WIDTH + X_SPACE), -1.0f + HEIGHT + BOTTOM_MARGIN,
                    WIDTH, HEIGHT,
                    0.85547f, 0.19531f, 0.21094f);
        }
    }

    public void draw()
    {
        for (int i = 0; i < numberOfLifes; i++)
        {
            mLifes[i].draw();
        }
    }

    public void death()
    {
        numberOfLifes--;
    }

    public int checkNumberOfLifes()
    {
        return numberOfLifes;
    }
}
