package in.mobileappdev.news.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

import in.mobileappdev.news.widget.NewsWidgetDataProvider;

/**
 * Udacity
 * Created by satyanarayana.avv on 08-01-2017.
 */

public class NewsWidgetService extends RemoteViewsService {
    private String TAG = "NewsWidgetService";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(TAG, "onGetViewFactory");
        return new NewsWidgetDataProvider(this, intent);
    }
}
