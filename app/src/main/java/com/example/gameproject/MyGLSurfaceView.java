package com.example.gameproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer(context);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float xpx = e.getX() - this.getLeft();
        float ypx = e.getY() - this.getTop();

        float w = this.getWidth();
        float h = this.getHeight();

        float x = (2.0f*xpx)/w - 1.0f;
        float y = -(2.0f*ypx)/h + 1.0f;

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                mRenderer.setPlatformX(x - 0.3f);
        }

        return true;
    }
}
