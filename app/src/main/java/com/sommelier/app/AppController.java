package com.sommelier.app;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


import com.sommelier.helper.ThemeHelper;


public class AppController extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Створюю власну тему для додатка
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String themePref = sharedPreferences.getString("theme", ThemeHelper.DEFAULT_MODE);
        ThemeHelper.applyTheme(themePref);
    }
}