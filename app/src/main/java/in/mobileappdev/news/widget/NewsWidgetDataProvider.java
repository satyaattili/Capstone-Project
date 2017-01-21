package in.mobileappdev.news.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import in.mobileappdev.news.R;
import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.ui.SourcesActivity;
import in.mobileappdev.news.utils.Constants;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Udacity
 * Created by satyanarayana.avv on 08-01-2017.
 */

public class NewsWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    private List<Article> mArticles = new ArrayList<>();
    private Context mContext = null;
    private Subscription subscription;

    public NewsWidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        initData();
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }


    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mArticles.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, mArticles.get(position).getTitle());

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        subscription = APIClient.getInstance()
                .getArticles("bbc-news", Constants.TOP_ARTICLES, Constants.NEWS_API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsArticlesListResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "In onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "In onError() " + e.getMessage());
                    }

                    @Override
                    public void onNext(NewsArticlesListResponse sources) {

                        mArticles.clear();
                        List<Article> articles = sources.getArticles();
                        Log.d(TAG, "In onNext()" + articles.size());
                        mArticles.addAll(articles);
                    }
                });

    }

}
