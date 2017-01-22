package in.mobileappdev.news.presenter;


import android.content.Context;

import java.io.IOException;

import in.mobileappdev.news.R;
import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.SourcesResponce;
import in.mobileappdev.news.utils.Utils;
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
    private SourceGridView sourceGridView;
    private Subscription subscription;
    private Context ctx;

    public NewsSourcesPresenter(SourceGridView sourceGridView) {
        this.sourceGridView = sourceGridView;
    }

    public NewsSourcesPresenter(Context ctx, SourceGridView sourceGridView) {
        this.sourceGridView = sourceGridView;
        this.ctx = ctx;
    }

    public void start() {
        getSources();
    }

    public void destroy() {
        if (!subscription.isUnsubscribed()) {
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        sourceGridView.hideLoading();
                        if (e instanceof HttpException) {
                            sourceGridView.showError(ctx.getString(R.string.error_connrcting_servers), 0);
                        } else if (e instanceof IOException) {
                            if (!Utils.inNetworkConnected()) {
                                sourceGridView.showError(ctx.getString(R.string.error_connection), 1);
                            } else {
                                sourceGridView.showError(ctx.getString(R.string.errr_network), 1);
                            }
                        } else {
                            sourceGridView.showError(ctx.getString(R.string.error_something_wrong), 2);
                        }
                    }

                    @Override
                    public void onNext(SourcesResponce sources) {
                        sourceGridView.hideLoading();
                        sourceGridView.hideError();
                        if (sources.getSources() != null && sources.getSources().size() > 0) {
                            sourceGridView.showSources(sources.getSources());
                        } else {
                            sourceGridView.showError(ctx.getString(R.string.error_no_newspapers_found), 4);
                        }
                    }
                });
    }
}
