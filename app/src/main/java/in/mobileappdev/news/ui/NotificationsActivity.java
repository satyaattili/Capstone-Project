package in.mobileappdev.news.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsArticlesListAdapter;
import in.mobileappdev.news.db.DatabaseHandler;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.utils.Constants;

public class NotificationsActivity extends AppCompatActivity implements NewsArticlesListAdapter.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.notificationsList)
    RecyclerView notificationListView;

    @BindView(R.id.error_layout)
    TextView errorText;

    private NewsArticlesListAdapter newsArticlesAdapter;
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

        DatabaseHandler handler = new DatabaseHandler(this);
        notifications = handler.getAllNotifications();

        if(notifications.size() == 0){
            notificationListView.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
            errorText.setText(R.string.no_notifications_msg);
        }

        newsArticlesAdapter = new NewsArticlesListAdapter(this, notifications);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notificationListView.setLayoutManager(layoutManager);
        notificationListView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
        notificationListView.setAdapter(newsArticlesAdapter);
        newsArticlesAdapter.setOnClickListener(this);
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
}
