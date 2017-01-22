package in.mobileappdev.news.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class NewsApp extends Application {

    private static NewsApp appInstance;
    private static SharedPreferences preferences;

    public static NewsApp getAppInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        preferences = getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }

    public boolean getLoginStatus() {
        return getPreferences().getBoolean("loggedInStatus", false);
    }

    public void setLoginStatus(boolean isLoggedIn) {
        getEditor().putBoolean("loggedInStatus", isLoggedIn).apply();
    }

    public boolean getFirstLaunch() {
        return getPreferences().getBoolean("first-launch", true);
    }

    public void setFirstLaunch(boolean isFirstLaunch) {
        getEditor().putBoolean("first-launch", isFirstLaunch).apply();
    }


}
