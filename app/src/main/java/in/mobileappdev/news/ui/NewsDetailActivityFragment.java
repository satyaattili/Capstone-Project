package in.mobileappdev.news.ui;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mobileappdev.news.R;
import in.mobileappdev.news.bus.ArticleListEvent;
import in.mobileappdev.news.bus.MainBus;
import in.mobileappdev.news.models.Article;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A placeholder fragment containing a simple view.
 */
public class NewsDetailActivityFragment extends Fragment {

  private String TAG = "NewsDetailActivityFragment";
  @BindView(R.id.imageView)
  ImageView newsImage;

  @BindView(R.id.articleTitle)
  TextView articleTitle;

  @BindView(R.id.articleAuthor)
  TextView articleAuthor;

  @BindView(R.id.articleDesc)
  TextView articleDescription;

  @BindView(R.id.collapsing_toolbar)
  CollapsingToolbarLayout collapsingToolbarLayout;

  @BindView(R.id.app_bar_layout)
  AppBarLayout appBarLayout;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  private String newsUrl;

  public NewsDetailActivityFragment() {
  }

  public static NewsDetailActivityFragment createInstance() {
    NewsDetailActivityFragment detailFragment = new NewsDetailActivityFragment();
    //  Bundle bundle = new Bundle();
    // bundle.putString(EXTRA_ATTRACTION, attractionName);
    //  detailFragment.setArguments(bundle);
    return detailFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
    ButterKnife.bind(this,view);
    return view;
  }

  @OnClick(R.id.readMoreButton)
  public void readMoreClick(View v){
    Intent i = new Intent(getActivity(), NewsDetailWebActivity.class);
    i.putExtra("url", newsUrl);
    startActivity(i);
  }

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void recieveArticle(ArticleListEvent articleListEvent) {
    final Article article = articleListEvent.getArticle();

    Glide.with(getActivity())
        .load(article.getUrlToImage())
        .placeholder(R.drawable.source_thumbnail)
        .crossFade()
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .into(newsImage);

    articleTitle.setText(article.getTitle());
    articleAuthor.setText(article.getAuthor());
    articleDescription.setText(article.getDescription());
    newsUrl = article.getUrl();

    appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      boolean isShow = false;
      int scrollRange = -1;

      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (scrollRange == -1) {
          scrollRange = appBarLayout.getTotalScrollRange();
        }
        if (scrollRange + verticalOffset == 0) {
          collapsingToolbarLayout.setTitle(article.getTitle());
          isShow = true;
        } else if(isShow) {
          collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
          isShow = false;
        }
      }
    });

  }
  @Override
  public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // Some small additions to handle "up" navigation correctly
        Intent upIntent = NavUtils.getParentActivityIntent(getActivity());
        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Check if up activity needs to be created (usually when
        // detail screen is opened from a notification or from the
        // Wearable app
        if (NavUtils.shouldUpRecreateTask(getActivity(), upIntent)
            || getActivity().isTaskRoot()) {

          // Synthesize parent stack
          TaskStackBuilder.create(getActivity())
              .addNextIntentWithParentStack(upIntent)
              .startActivities();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          // On Lollipop+ we finish so to run the nice animation
          getActivity().finishAfterTransition();
          return true;
        }

        // Otherwise let the system handle navigating "up"
        return false;
    }
    return super.onOptionsItemSelected(item);
  }


}
