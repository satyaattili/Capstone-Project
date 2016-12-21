package in.mobileappdev.news.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class NewsApp extends Application {

  private static NewsApp appInstance;

  @Override
  public void onCreate() {
    super.onCreate();
    appInstance = this;
  }

  public static NewsApp getAppInstance(){
    return appInstance;
  }
}
