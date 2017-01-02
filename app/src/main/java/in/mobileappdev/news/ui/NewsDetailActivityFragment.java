package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.mobileappdev.news.R;
import in.mobileappdev.news.bus.ArticleEvent;
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

    private static String KEY_ARTICLE = "article";
    private Article article;

    public NewsDetailActivityFragment() {
    }

    public static NewsDetailActivityFragment createInstance(Article article) {
        NewsDetailActivityFragment newsDeatailFragment = new NewsDetailActivityFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARTICLE, article);
        newsDeatailFragment.setArguments(bundle);
        return newsDeatailFragment;
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

        article = (Article) getArguments().getParcelable(KEY_ARTICLE);
        bindArticleData(article);

        AdRequest adRequest = new AdRequest.Builder().build();
        bannerAdView.loadAd(adRequest);

        return view;
    }

    @OnClick({R.id.readMoreButton, R.id.sharebtn})
    public void readMoreClick(View v) {
        int id = v.getId();
        if (id == R.id.readMoreButton) {
            Intent i = new Intent(getActivity(), NewsDetailWebActivity.class);
            i.putExtra(Constants.URL, newsUrl);
            startActivity(i);
        } else {
            Intent sendIntent = new Intent();
            String msg = Constants.DYNAMIC_LINK_DOMAIN + "?link=" + Constants
                    .BASE_SHARE_URL + newsUrl + "&apn=" + Constants.PACKAGE_NAME;
            Log.d(TAG, "Shared URL : " + msg);
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }

    }

    private void bindArticleData(Article article) {
        Glide.with(getActivity())
                .load(article.getUrlToImage())
                .placeholder(R.drawable.detail_image_placeholder)
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(newsImage);

        articleTitle.setText(article.getTitle());
        articleAuthor.setText(article.getAuthor());
        articleDescription.setText(article.getDescription());
        newsUrl = article.getUrl();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void recieveArticle(ArticleEvent articleEvent) {
        final Article article = articleEvent.getArticle();

        Glide.with(getActivity())
                .load(article.getUrlToImage())
                .placeholder(R.drawable.detail_image_placeholder)
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
