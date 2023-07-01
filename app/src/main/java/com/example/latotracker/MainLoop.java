package com.example.latotracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaRecorder;

import com.example.latotracker.objects.XFont;
import com.example.latotracker.utils.Sprites;

public class MainLoop {

    public boolean active = false, visible = false;
    // 16600 - (amplitude where human voice is barely heard but lato lato sound is)
    final double THRESHOLD = 15600, MAXTHRESHOLD = 25000;

    MediaRecorder mr;

    // for logic
    double lastLatoTime, gap;
    int lato;

    // for design/animation
    boolean alive, recording;
    double waveSize, minWaveSize, maxWaveSize, waveAnimationLastTime;
    int waveFrame;
    XFont latoFont, statusFont;

    public MainLoop() {
        lato = 0;
        lastLatoTime = 0;
        alive = false;
        recording = false;

        // initializing sound wave size
        minWaveSize = Game.screen.w * 0.9;
        maxWaveSize = Game.screen.w * 1.2;
        waveSize = minWaveSize;

        // initializing font renderers
        latoFont =  new XFont(R.drawable.font_sprite_kyokasho_black, waveSize * 0.30);
        statusFont =  new XFont(R.drawable.font_sprite_kyokasho_black, latoFont.height * 0.25);

        // changing the size of the background image
        int newW = Sprites.background_bokeh.getWidth();
        int newH = Sprites.background_bokeh.getHeight();

        while (newW < Game.screen.w || newH < Game.screen.h) {
            newW *= 1.01;
            newH *= 1.01;
        }

        Sprites.background_bokeh = Bitmap.createScaledBitmap(
            Sprites.background_bokeh,
            newW,
            newH,
            false
        );

        // debug
        recording = startRecording();
        while (!recording) {
            recording = startRecording();
        }

        // initialization for animation
        waveAnimationLastTime = System.currentTimeMillis();
    }


    public void update() {
        if (!active) return;

        int amp = mr.getMaxAmplitude();

        if (amp > THRESHOLD) {
            if (lastLatoTime == 0) {
                lastLatoTime = System.currentTimeMillis();
                alive = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Sprites.font_sprites[latoFont.id] = BitmapFactory.decodeResource(Game.res, R.drawable.font_sprite_kyokasho_green, options);
            }

            gap = System.currentTimeMillis() - lastLatoTime;

            if (gap >= 10 && gap <= 250) {
                lato += 1;
            }
            else if (gap >= 1000) {
                lato = 0;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Sprites.font_sprites[latoFont.id] = BitmapFactory.decodeResource(Game.res, R.drawable.font_sprite_kyokasho_green, options);
                alive = true;
            }

            // sound wave animation
            double newWaveSize = minWaveSize + (maxWaveSize - minWaveSize) * amp/MAXTHRESHOLD;
            if (newWaveSize > waveSize && recording) {
                waveSize = newWaveSize;
                // debug (or optimize. coz this is not optimized)
                latoFont.setHeight(waveSize * 0.3);
                statusFont.setHeight(latoFont.height * 0.25);
            }

//            Log.d("huling.lato", "" + gap);
            lastLatoTime = System.currentTimeMillis();
        }

        if (alive) {
            double gapTest = System.currentTimeMillis() - lastLatoTime;
            if (gapTest >= 1000) {
                alive = false;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                Sprites.font_sprites[latoFont.id] = BitmapFactory.decodeResource(Game.res, R.drawable.font_sprite_kyokasho_red, options);
            }
        }

        // animation thingies
            // sound wave
        double animationTime = System.currentTimeMillis() - waveAnimationLastTime;

        if (animationTime >= 1500) {
            waveAnimationLastTime = System.currentTimeMillis();
        }

        if (recording) {
            waveFrame = (int)((System.currentTimeMillis() - waveAnimationLastTime)/1500 * Sprites.sound_wave_frames.length);
        }

        if (waveSize > minWaveSize) {
            waveSize *= 0.98;
            // debug (or optimize. coz this is not optimized)
            latoFont.setHeight(waveSize * 0.3);
            statusFont.setHeight(latoFont.height * 0.25);
        }
        else {
            waveSize = minWaveSize;
        }

    }



    public void draw(Canvas canvas) {
        Paint paint = new Paint();

        // background
        canvas.drawBitmap(Sprites.background_bokeh, 0, 0, null);

        // counter
        int y = (int) (Game.screen.h/2 - latoFont.height/2 - latoFont.height*0.10);
        latoFont.render("" + lato, (int) (Game.screen.w/2 - latoFont.strWidth(""+lato)/2), y, canvas);

        // status
        String status = recording ? "listening" : "silent";
        y = (int) (y + latoFont.height + statusFont.height*0.05);
        statusFont.render(status, (int) (Game.screen.w/2 - statusFont.strWidth(status)/2), y, canvas);

        // wave thingy

        Bitmap bmp = Bitmap.createScaledBitmap(Sprites.sound_wave_frames[waveFrame], (int) waveSize, (int) waveSize, false);

        canvas.drawBitmap(
            bmp,
            (float) (Game.screen.w/2 - bmp.getWidth()/2),
            (float) (Game.screen.h/2 - bmp.getHeight()/2),
        null
        );

    }


    public boolean startRecording() {
        try {
            mr = new MediaRecorder();
            mr.setAudioSource(MediaRecorder.AudioSource.MIC);
            mr.setAudioSamplingRate(44100);
            mr.setAudioChannels(1);
            mr.setAudioEncodingBitRate(16);
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mr.setOutputFile(Game.context.getCacheDir() + "/temp.mp3");
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mr.prepare();
            mr.start();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public void stopRecording() {
        mr.stop();
        mr.release();
        mr = null;
        recording = false;
    }

}
