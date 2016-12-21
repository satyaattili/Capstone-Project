package in.mobileappdev.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.mobileappdev.news.app.NewsApp;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class Utils {

  public static boolean inNetworkConnected() {
    ConnectivityManager
        cm = (ConnectivityManager) NewsApp.getAppInstance().getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null
        && activeNetwork.isConnectedOrConnecting();
  }

}
