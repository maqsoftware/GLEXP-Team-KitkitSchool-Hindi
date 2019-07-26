/**
 * VoiceMoldManager.java -- Thin wrapper for locale-to-tts mapping
 * <p>
 * NB(xenosoz, 2018): Date created: 26 Feb, 2018
 */


package org.cocos2dx.cpp;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;


public class VoiceMoldManager {
    // ----
    private static VoiceMoldManager _shared;
    public String defaultLocale = "hi-IN";
    public HashMap<String, VoiceMold> moldForLocaleCache;

    public VoiceMoldManager() {
        moldForLocaleCache = new HashMap<String, VoiceMold>();
    }

    public static VoiceMoldManager shared() {
        if (_shared == null) {
            _shared = new VoiceMoldManager();
        }
        return _shared;
    }

    public static String staticGetDefaultLocale() {
        return shared().getDefaultLocale();
    }

    public static void staticSetDefaultLocale(String locale) {
        shared().setDefaultLocale(locale);
    }

    public static void staticSecureVoiceData() {
        shared().secureVoiceData();
    }

    public static void staticSecureVoiceData(String locale) {
        shared().secureVoiceData(locale);
    }

    public static void staticWarmup() {
        shared().warmup();
    }

    public static void staticWarmup(String locale) {
        shared().warmup(locale);
    }

    public static void staticSpeak(String text) {
        shared().speak(text);
    }

    public static void staticSpeak(String text, String locale) {
        shared().speak(text, locale);
    }

    public static float staticGuessSpeakDuration(String text) {
        return shared().guessSpeakDuration(text);
    }

    public static float staticGuessSpeakDuration(String text, String locale) {
        return shared().guessSpeakDuration(text);
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }


    // NB(xenosoz, 2018): Sugers for simplifying exposure to c++ world.

    public void setDefaultLocale(String locale) {
        defaultLocale = locale;
    }

    public VoiceMold moldForLocale(String locale) {
        VoiceMold cache = moldForLocaleCache.get(locale);
        if (cache != null) {
            return cache;
        }

        VoiceMold mold = new VoiceMold(locale);
        moldForLocaleCache.put(locale, mold);
        return mold;
    }

    public void secureVoiceData() {
        secureVoiceData(defaultLocale);
    }

    public void secureVoiceData(String locale) {
        VoiceMold mold = moldForLocale(locale);

        if (mold != null && mold.isEnergetic()) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction("com.maq.voice_engine_a.installer.CALL_BY_MAQ_SOFTWARE");

        Context context = AppActivity.instance();
        context.startActivity(intent);
    }

    public void warmup() {
        warmup(defaultLocale);
    }

    public void warmup(String locale) {
        VoiceMold mold = moldForLocale(locale);
        mold.warmup();
    }

    public void speak(String text) {
        // to handle Bengali accent issue while speaking English
        if (defaultLocale.equals("bn-IN") && ((!text.equals("")) && (text != null) && (text.matches("^[a-zA-Z0-9_ ?!.,'\"]*$")))) {
            speak(text, "hi-IN");
        } else {
            speak(text, defaultLocale);
        }
    }

    public void speak(String text, String locale) {
        VoiceMold mold = moldForLocale(locale);
        mold.speak(text);
    }

    public float guessSpeakDuration(String text) {
        return guessSpeakDuration(text, defaultLocale);
    }

    public float guessSpeakDuration(String text, String locale) {
        VoiceMold mold = moldForLocale(locale);
        return mold.guessSpeakDuration(text);
    }
}
