package com.example.latotracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.latotracker.objects.Rect;
import com.example.latotracker.utils.Sprites;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    public static MainLoop mainLoop;
    public static Context context;
    public static Resources res;
    public static Rect screen;
    GameLoop gameLoop;

    public Game(Context context) {
        super(context);

        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this, surfaceHolder);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("Game.java", "surfaceCrated()");

        // initializing static variables
        this.res = getResources();
        this.context = getContext();
        this.screen = new Rect(0, 0, getWidth(), getHeight());
        Sprites.initialize();

        // initializing game panels
        this.mainLoop = new MainLoop();
        mainLoop.active = true;
        mainLoop.visible = true;

        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    public void update() {

        // updating the panels
        mainLoop.update();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // drawing the panels
        mainLoop.draw(canvas);

    }
}
