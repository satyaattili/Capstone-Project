package in.mobileappdev.news.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsArticleslistAdapter;
import in.mobileappdev.news.db.NewsContract;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.utils.Utils;

public class NotificationsActivity extends AppCompatActivity implements NewsArticleslistAdapter.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 987;
    private final String TAG = NotificationsActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notificationsList)
    RecyclerView notificationListView;
    @BindView(R.id.error_layout)
    TextView errorText;
    @BindView(R.id.notification_loading)
    ProgressBar progressBar;
    private NewsArticleslistAdapter newsArticlesAdapter;
    private ArrayList<Article> notifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.notifications);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        notifications = new ArrayList<>();

        newsArticlesAdapter = new NewsArticleslistAdapter(this, notifications);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationListView.setLayoutManager(layoutManager);
        notificationListView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
        notificationListView.setAdapter(newsArticlesAdapter);
        newsArticlesAdapter.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onClick(View v, int position) {
        String url = notifications.get(position).getUrl();
        Intent intent = new Intent(NotificationsActivity.this, NewsDetailWebActivity.class);
        intent.putExtra(Constants.URL, url);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                NewsContract.CONTENT_URI
                , null, null, null, null);
    }

    /*@Override
        public Loader onCreateLoader(int id, Bundle args) {
            return new CursorLoader(this,
                    NewsContract.CONTENT_URI
                    , new String[]{"col1"}, null, null, null);
        }
    */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        progressBar.setVisibility(View.GONE);
        if (cursor != null) {
            notifications.clear();
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //author,tile, des, url, urlImage, time
                    if (!Utils.isEmpty(cursor.getString(2))) {
                        Article article = new Article();
                        article.setTitle(cursor.getString(2));
                        article.setDescription(cursor.getString(2));
                        article.setUrl(cursor.getString(1));
                        article.setUrlToImage(cursor.getString(3));
                        article.setPublishedAt(cursor.getString(4));
                        notifications.add(article);
                        Log.e(TAG, article.toString());
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (notifications.size() == 0) {
            notificationListView.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_notifications_msg);
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
