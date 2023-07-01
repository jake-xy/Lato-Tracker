package com.example.latotracker.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.latotracker.Game;
import com.example.latotracker.R;

public class Sprites {

    public static Bitmap
        background_bokeh
    ;

    public static Bitmap[]
        font_sprites = new Bitmap[0],
        sound_wave_frames = new Bitmap[28];
    ;


    public static void initialize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        // background
        background_bokeh = BitmapFactory.decodeResource(Game.res, R.drawable.bg_bokeh, options);

        // circular sound wave
        for (int i = 0; i < sound_wave_frames.length; i++) {
            int id = Game.res.getIdentifier("f" + i, "drawable", Game.context.getPackageName());
            sound_wave_frames[i] = BitmapFactory.decodeResource(Game.res, id, options);
        }

        System.out.println("------------------------------------------------");
        System.out.println(sound_wave_frames[0].getWidth());
    }

}
