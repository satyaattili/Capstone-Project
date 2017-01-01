package in.mobileappdev.news.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.SourcesResponce;
import in.mobileappdev.news.views.SourceGridView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by satyanarayana.avv on 22-12-2016.
 */

public class NewsSourcesPresenter {

  private String TAG = NewsSourcesPresenter.class.getSimpleName();
  @NonNull
  private SourceGridView sourceGridView;
  @NonNull
  private Subscription subscription;

  public NewsSourcesPresenter(@NonNull SourceGridView sourceGridView) {
    this.sourceGridView = sourceGridView;
  }

  public void start() {
    getSources();
  }

  public void destroy() {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }

  private void getSources() {
    sourceGridView.showLoading();
    sourceGridView.hideError();
    subscription = APIClient.getInstance()
        .getNewsSources("en", "")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<SourcesResponce>() {
          @Override
          public void onCompleted() {
            Log.d(TAG, "In onCompleted()");
          }

          @Override
          public void onError(Throwable e) {
            sourceGridView.hideLoading();
            if (e instanceof HttpException) {
              sourceGridView.showError("HTTP Error", 0);
            } else if (e instanceof IOException) {
              sourceGridView.showError("Network Error", 1);
            } else {
              sourceGridView.showError("Unknown Error", 2);
            }
          }

          @Override
          public void onNext(SourcesResponce sources) {
            sourceGridView.hideLoading();
            sourceGridView.hideError();
            Log.d(TAG, "In onNext()" + sources.getSources().size());
            if (sources.getSources().size() > 0) {
              sourceGridView.showSources(sources.getSources());
            }else{
              sourceGridView.showError("No Sources found",4);
            }
          }
        });
  }
}
