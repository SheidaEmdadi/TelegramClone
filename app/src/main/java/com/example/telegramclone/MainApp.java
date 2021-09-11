package com.example.telegramclone;

import android.app.Application;

import sdk.chat.app.firebase.ChatSDKFirebase;
import sdk.chat.core.session.ChatSDK;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            ChatSDKFirebase.quickStart(MainApp.this, "pre_1", "abababab", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ChatSDK.ui().startSplashScreenActivity(this);
    }
}
