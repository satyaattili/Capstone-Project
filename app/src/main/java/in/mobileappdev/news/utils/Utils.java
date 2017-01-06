package in.mobileappdev.news.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.mobileappdev.news.app.NewsApp;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class Utils {

  private static String TAG = "Utils";

  public static boolean inNetworkConnected() {
    ConnectivityManager
        cm = (ConnectivityManager) NewsApp.getAppInstance().getApplicationContext()
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    return activeNetwork != null
        && activeNetwork.isConnectedOrConnecting();
  }

  public static String getTimeString(String time){
    if(isEmpty(time)){
      return "";
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd'T'HH:mm:ss'Z'",
        Locale.getDefault());
    Date convertedDate;
    try {
      convertedDate = dateFormat.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
      return "";
    }
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
    return df.format(convertedDate);
  }

  public static boolean isEmpty(String str) {
    return !(str != null && !str.trim().equals(Constants.EMPTY_STRING));
  }
}

