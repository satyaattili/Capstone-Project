package in.mobileappdev.news.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.views.ArticleListView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by satyanarayana.avv on 22-12-2016.
 */

public class NewsArticlesPresenter {

  private String TAG = NewsArticlesPresenter.class.getSimpleName();
  @NonNull
  private ArticleListView articleListView;
  @NonNull
  private String sourceId;
  private Subscription subscription;

  public NewsArticlesPresenter(@NonNull ArticleListView articleListView, @NonNull String sourceId) {
    this.articleListView = articleListView;
    this.sourceId = sourceId;
  }

  public void start() {
    getArticles();
  }

  public void destroy() {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  private void getArticles() {
    articleListView.showLoading();
    subscription = APIClient.getInstance()
        .getArticles(sourceId, "top", Constants.NEWS_API_KEY)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<NewsArticlesListResponse>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "In onCompleted()");
          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
            Log.d(TAG, "In onError() " + e.getMessage());
          }

          @Override
          public void onNext(NewsArticlesListResponse sources) {
            Log.d(TAG, "In onNext()" + sources.getArticles().size());
            if (sources.getArticles() != null && sources.getArticles().size() > 0) {
              articleListView.hideLoading();
              articleListView.showArticles(sources.getArticles());
            }
          }
        });
  }
}
