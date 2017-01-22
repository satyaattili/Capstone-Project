package in.mobileappdev.news.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import in.mobileappdev.news.R;
import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.utils.Utils;
import in.mobileappdev.news.views.ArticleListView;
import retrofit2.adapter.rxjava.HttpException;
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
    private Context ctx;

    public NewsArticlesPresenter(@NonNull ArticleListView articleListView, @NonNull String sourceId) {
        this.articleListView = articleListView;
        this.sourceId = sourceId;
    }

    public NewsArticlesPresenter(Context ctx, @NonNull ArticleListView articleListView, @NonNull String sourceId) {
        this.articleListView = articleListView;
        this.sourceId = sourceId;
        this.ctx = ctx;
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
                .getArticles(sourceId, Constants.TOP_ARTICLES, Constants.NEWS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsArticlesListResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        articleListView.hideLoading();
                        Log.d(TAG, "In onError() " + e.getMessage());
                        if (e instanceof HttpException) {
                            articleListView.showError(ctx.getString(R.string.error_connrcting_servers), 0);
                        } else if (e instanceof IOException) {
                            if (!Utils.inNetworkConnected()) {
                                articleListView.showError(ctx.getString(R.string.error_connection), 1);
                            } else {
                                articleListView.showError(ctx.getString(R.string.errr_network), 1);
                            }
                        } else {
                            articleListView.showError(ctx.getString(R.string.error_something_wrong), 2);
                        }
                    }

                    @Override
                    public void onNext(NewsArticlesListResponse sources) {
                        Log.d(TAG, "In onNext()" + sources.getArticles().size());
                        if (sources.getArticles() != null && sources.getArticles().size() > 0) {
                            articleListView.hideLoading();
                            articleListView.hideError();
                            articleListView.showArticles(sources.getArticles());
                        }
                    }
                });
    }
}
