package com.example.latotracker.objects;

import static com.example.latotracker.utils.Utils.*;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.example.latotracker.Game;
import com.example.latotracker.utils.Sprites;

public class XFont {

    final static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    static int[] widthOfChars = new int[chars.length()];
    static int[] xOfChars = new int[chars.length()];
    static int spriteHeight = 0, ID = 0;
    public int id;
    int fontSpriteID;

    public double height, charSpacing = 4;

    public XFont(int fontSpriteID, double height) {
        this.height = height;
        this.fontSpriteID = fontSpriteID;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap sprite = BitmapFactory.decodeResource(Game.res, fontSpriteID, options);

        Sprites.font_sprites = append(sprite, Sprites.font_sprites);

        // initialize the width of every char
        int idxCount = 0, startPixel = 0, width = 0;
        for (int x = 0; x < sprite.getWidth(); x++) {
            int color = sprite.getPixel(x, 0);
            int r = Color.red(color), g = Color.green(color), b = Color.blue(color), a = Color.alpha(color);

            // get the widths of each char
            if (r == 118 && g == 118 && b == 118) {
                if (x > startPixel) {
                    width = x - startPixel;
                    widthOfChars[idxCount] = width;
                    xOfChars[idxCount] = startPixel;
                    startPixel = x + 1; // in the sprites, each char is separated by 1 pixel of gray
                    idxCount += 1;
                }
            }
        }
        spriteHeight = sprite.getHeight();

        id = ID;
        ID += 1;
    }

    public void render(String text, int xPos, int yPos, Canvas canvas) {

        Bitmap sprite = Sprites.font_sprites[id];

        int x = xPos;

        for (int i = 0; i < text.length(); i++) {
            int charI = XFont.chars.indexOf(text.charAt(i));

            // get scaled width and height
            int scaledH = (int) this.height;
            int scaledW = widthOfChars[charI] * scaledH / sprite.getHeight();

            Bitmap bmp = Bitmap.createBitmap(sprite, xOfChars[charI], 0, widthOfChars[charI], sprite.getHeight());
            bmp = Bitmap.createScaledBitmap(bmp, scaledW, scaledH, false);

            canvas.drawBitmap(bmp, x, yPos, null);

            x += scaledW + charSpacing;
        }

    }

    public int strWidth(String text) {
        int out = 0;

        for (int i = 0; i < text.length(); i++) {
            int charI = XFont.chars.indexOf(text.charAt(i));

            // get scaled width and height
            int scaledH = (int) this.height;
            int scaledW = widthOfChars[charI] * scaledH / spriteHeight;

            out += scaledW + charSpacing;
        }

        return out;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setCharSpacing(double pixels) {
        this.charSpacing = pixels;
    }

    public void setColor(int r, int g, int b) {

        double w = Sprites.font_sprites[id].getWidth();
        double h = Sprites.font_sprites[id].getHeight();

        Bitmap newBmp = Sprites.font_sprites[id].copy(Sprites.font_sprites[id].getConfig(), true);

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int pixel = Sprites.font_sprites[id].getPixel(col, row);

                if (Color.alpha(pixel) == 0) continue;

                newBmp.setPixel(col, row, Color.argb(255, r, g, b));
            }
        }

        Sprites.font_sprites[id] = newBmp.copy(newBmp.getConfig(), false);
    }


}
