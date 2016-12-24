package in.mobileappdev.news.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.bus.ArticleListEvent;

public class NewsDetailActivity extends AppCompatActivity {

  //@BindView(R.id.toolbar) Toolbar toolbar;

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void launch(Activity activity, View heroView) {
    Intent intent = getLaunchIntent(activity);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     /* ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
          activity, heroView, heroView.getTransitionName());
      ActivityCompat.startActivity(activity, intent, options.toBundle());*/
      activity.startActivity(intent);
    } else {
      activity.startActivity(intent);
    }
  }

  public static Intent getLaunchIntent(Context context) {
    Intent intent = new Intent(context, NewsDetailActivity.class);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_detail);
    ButterKnife.bind(this);
   // setSupportActionBar(toolbar);

   if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .add(R.id.container, NewsDetailActivityFragment.createInstance())
          .commit();
    }

  }
}
