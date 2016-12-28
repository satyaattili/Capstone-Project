package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import in.mobileappdev.news.BuildConfig;
import in.mobileappdev.news.R;

public class SplashScreenActivity extends AppCompatActivity {

  private static final String LOGIN_CONFIG = "login";
  FirebaseRemoteConfig firebaseRemoteConfig;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash_screen);

    firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
    firebaseRemoteConfig.setConfigSettings(configSettings);
    firebaseRemoteConfig.setDefaults(R.xml.default_remote_config);

    fetchLoginConfig();
  }

  private void fetchLoginConfig(){

    long cacheExpiration = 3600; // 1 hour in seconds.
    if (firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
      cacheExpiration = 0;
    }
    firebaseRemoteConfig.fetch(cacheExpiration)
        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
          @Override
          public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
              Toast.makeText(SplashScreenActivity.this, "Fetch Succeeded",
                  Toast.LENGTH_SHORT).show();
              firebaseRemoteConfig.activateFetched();
            } else {
              Toast.makeText(SplashScreenActivity.this, "Fetch Failed",
                  Toast.LENGTH_SHORT).show();
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
        if(firebaseRemoteConfig.getBoolean(LOGIN_CONFIG)){
          i = new Intent(SplashScreenActivity.this, LoginActivity.class);
          Toast.makeText(SplashScreenActivity.this, "Login Needed",
              Toast.LENGTH_SHORT).show();

        }else{
          i = new Intent(SplashScreenActivity.this, MainActivity.class);
          Toast.makeText(SplashScreenActivity.this, "Login Not Neeeded",
              Toast.LENGTH_SHORT).show();
        }

        startActivity(i);
        finish();
      }
    }, SPLASH_TIME_OUT);
  }
}
