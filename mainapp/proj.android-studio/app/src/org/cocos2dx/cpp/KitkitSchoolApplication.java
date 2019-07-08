package org.cocos2dx.cpp;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import com.maq.kitkitProvider.KitkitDBHandler;
import com.maq.kitkitlogger.KitKitLogger;

/**
 * Created by ingtellect on 7/24/17.
 */

public class KitkitSchoolApplication extends Application {
    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    private KitKitLogger logger;

    private KitkitDBHandler dbHandler;

    AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
        logger = new KitKitLogger(getPackageName(), getApplicationContext());

        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        dbHandler = new KitkitDBHandler(getApplicationContext());

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(thread, e);
            }
        });

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);                     //maximum value of stream media.
        if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < maxVolume / 2) {                  //check if the audio is less than 50%
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 2, 0);     //set the audio to 50% when app start.
        }
    }

    public void handleUncaughtException(Thread thread, Throwable e) {
        e.printStackTrace(); // not all Android versions will print the stack trace automatically

        logger.extractLogToFile();

        defaultExceptionHandler.uncaughtException(thread, e);
    }

    public KitKitLogger getLogger() {
        return logger;
    }

    public KitkitDBHandler getDbHandler() {
        return dbHandler;
    }
}
