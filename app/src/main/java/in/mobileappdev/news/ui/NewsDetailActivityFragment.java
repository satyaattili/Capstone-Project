package in.mobileappdev.news.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.Actions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mobileappdev.news.R;
import in.mobileappdev.news.bus.ArticleListEvent;
import in.mobileappdev.news.firebase.AppIndexingHelper;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.utils.Constants;


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

  @BindView(R.id.ad_view)
  AdView bannerAdView;

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
    ButterKnife.bind(this, view);

    AdRequest adRequest = new AdRequest.Builder().build();
    bannerAdView.loadAd(adRequest);

    return view;
  }

  @OnClick(R.id.readMoreButton)
  public void readMoreClick(View v) {
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
        .centerCrop()
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .into(newsImage);

    articleTitle.setText(article.getTitle());
    articleAuthor.setText(article.getAuthor());
    articleDescription.setText(article.getDescription());
    newsUrl = article.getUrl();

    //setOffsetChangeListner(article);

  }


  /**
   * Called before the activity is destroyed
   */
  @Override
  public void onDestroy() {
    if (bannerAdView != null) {
      bannerAdView.destroy();
    }
    super.onDestroy();
  }

/*
  private void setOffsetChangeListner(final Article article) {
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
*/

  @Override
  public void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
    if (bannerAdView != null) {
      bannerAdView.pause();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
    if (bannerAdView != null) {
      bannerAdView.resume();
    }
  }

  @Override
  public void onStart() {
    super.onStart();
    AppIndexingHelper.startAppIndexing(articleTitle.getText().toString(), newsUrl, getActivity());
  }

  @Override
  public void onStop() {
    super.onStop();
    AppIndexingHelper.endAppIndexing(articleTitle.getText().toString(), newsUrl, getActivity());
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          getActivity().finishAfterTransition();
          return true;
        }

        return false;
    }
    return super.onOptionsItemSelected(item);
  }


}
