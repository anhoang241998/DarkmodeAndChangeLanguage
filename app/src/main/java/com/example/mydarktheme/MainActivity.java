package com.example.mydarktheme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Switch mSwitchNightMode, mSwitchLanguage;
    private SharedPreferences mAppSettingPrefs;
    private SharedPreferences.Editor mSharedPrefsEdit;
    private Boolean isNightModeOn;
    private Boolean isLanguageChange;
    private String mLanguageString;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppSettingPrefs = getSharedPreferences("AppSettingPrefs", 0);
        mSharedPrefsEdit = mAppSettingPrefs.edit();
        isNightModeOn = mAppSettingPrefs.getBoolean("NightMode", false);
        mLanguageString = mAppSettingPrefs.getString("Language", "");
        isLanguageChange = mAppSettingPrefs.getBoolean("LanguageChange", false);
        if (isLanguageChange) {
            setApplocale(mLanguageString);
        } else {
            setApplocale(mLanguageString);
        }

        setContentView(R.layout.activity_main);


        mSwitchNightMode = findViewById(R.id.switchNightMode);
        mSwitchLanguage = findViewById(R.id.switchLanguage);

        if (isNightModeOn) {
            mSwitchNightMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            mSwitchNightMode.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
        if (isLanguageChange) {
            mSwitchLanguage.setChecked(true);
        } else {
            mSwitchLanguage.setChecked(false);
        }
        mSwitchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    mSharedPrefsEdit.putBoolean("NightMode", true);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    mSharedPrefsEdit.putBoolean("NightMode", false);
                }
                mSharedPrefsEdit.apply();
                finish();
                restartCurrentActivity();
            }
        });

        mSwitchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setApplocale("vi");
                    mSharedPrefsEdit.putBoolean("LanguageChange", true);
                    mSharedPrefsEdit.putString("Language", "vi");
                    mSharedPrefsEdit.apply();
                    finish();
                    restartCurrentActivity();
                } else {
                    setApplocale("en");
                    mSharedPrefsEdit.putBoolean("LanguageChange", false);
                    //đây là chỗ em lưu sharedpreference sau đó call lại khi kill app
                    mSharedPrefsEdit.putString("Language", "en");
                    mSharedPrefsEdit.apply();
                    finish();
                    restartCurrentActivity();
                }
            }
        });
    }


    private void restartCurrentActivity() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    private void setApplocale(final String langCode) {
        Resources activityRes = getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale(langCode , "VN");
        activityConf.setLocale(newLocale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = getApplicationContext().getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(newLocale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
    }
}
