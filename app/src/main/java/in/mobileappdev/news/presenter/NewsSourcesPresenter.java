package in.mobileappdev.news.presenter;


import java.io.IOException;

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

    public NewsSourcesPresenter(SourceGridView sourceGridView) {
        this.sourceGridView = sourceGridView;
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
                            sourceGridView.showError("Problem with connecting our servers", 0);
                        } else if (e instanceof IOException) {
                            if (!Utils.inNetworkConnected()) {
                                sourceGridView.showError("No internet connection, \nCheck your WI-FI or Data connection", 1);
                            } else {
                                sourceGridView.showError("Network Problem", 1);
                            }
                        } else {
                            sourceGridView.showError("Something wrong happened \nPlease try again", 2);
                        }
                    }

                    @Override
                    public void onNext(SourcesResponce sources) {
                        sourceGridView.hideLoading();
                        sourceGridView.hideError();
                        if (sources.getSources()!=null && sources.getSources().size() > 0) {
                            sourceGridView.showSources(sources.getSources());
                        } else {
                            sourceGridView.showError("No News papers found "+sources.getStatus(), 4);
                        }
                    }
                });
    }
}
