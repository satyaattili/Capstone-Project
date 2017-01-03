package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import in.mobileappdev.news.BuildConfig;
import in.mobileappdev.news.R;
import in.mobileappdev.news.utils.Constants;

public class SplashScreenActivity extends AppCompatActivity {

  private String TAG = SplashScreenActivity.class.getSimpleName();
  private static final String LOGIN_CONFIG = "login";
  private FirebaseRemoteConfig firebaseRemoteConfig;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);
    onNewIntent(getIntent());

    firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
    firebaseRemoteConfig.setConfigSettings(configSettings);
    firebaseRemoteConfig.setDefaults(R.xml.default_remote_config);

    fetchLoginConfig();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void fetchLoginConfig() {

    long cacheExpiration = 3600; // 1 hour in seconds.
    if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }
    firebaseRemoteConfig.fetch(cacheExpiration)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Log.d(TAG, "Remote Config Fetch Succeeded");
              firebaseRemoteConfig.activateFetched();
            } else {
              Log.d(TAG, "Remote Config Fetch Failed");
            }
            goToNextScreen();
          }
        });
  }

  private void goToNextScreen() {
    int SPLASH_TIME_OUT = 1000;
    new Handler().postDelayed(new Runnable() {

      @Override
      public void run() {
        Intent i;
        if (firebaseRemoteConfig.getBoolean(LOGIN_CONFIG)) {
          i = new Intent(SplashScreenActivity.this, LoginActivity.class);
          Log.d(TAG, "Remote Config : LOGIN NEEDED");
        } else {
          i = new Intent(SplashScreenActivity.this, SourcesActivity.class);
          Log.d(TAG, "Remote Config : LOGIN NOT NEEDED");
        }

        startActivity(i);
        finish();
      }
    }, SPLASH_TIME_OUT);
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    Bundle bundle = intent.getExtras();
    if(bundle == null){
      return;
    }
    for (String key : bundle.keySet()) {
      Log.d(TAG, "KEY : "+key+ " "+bundle.get(key).toString());
    }
    if(intent.getStringExtra(Constants.DEEPLINK) != null){
      Intent i = new Intent(this, NewsDetailWebActivity.class);
      i.putExtra(Constants.URL, intent.getStringExtra(Constants.DEEPLINK));
      startActivity(i);
    }
  }
}
