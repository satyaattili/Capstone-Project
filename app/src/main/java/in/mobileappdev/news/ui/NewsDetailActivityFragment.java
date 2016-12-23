package in.mobileappdev.news.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_news_detail, container, false);
    ButterKnife.bind(this,view);
    return view;
  }

  private Subscriber<? super Object> mainBusSubscriber = new Subscriber<Object>() {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(Object o) {
      if (o instanceof ArticleListEvent) {
        Log.e(TAG, "Recieved");
      }
    }
  };

  @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
  public void recieveArticle(ArticleListEvent articleListEvent) {
    Glide.with(getActivity())
        .load(articleListEvent.getArticle().getUrlToImage())
        .placeholder(R.drawable.source_thumbnail)
        .crossFade()
        .diskCacheStrategy(DiskCacheStrategy.RESULT)
        .into(newsImage);

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

}
