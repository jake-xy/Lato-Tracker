package com.example.latotracker.utils;

import android.graphics.Bitmap;

public abstract class Utils {


    public static Bitmap[] append(Bitmap item, Bitmap[] array) {
        Bitmap[] out = new Bitmap[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }

}
