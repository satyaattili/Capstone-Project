package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsArticleslistAdapter;
import in.mobileappdev.news.bus.ArticleListEvent;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.presenter.NewsArticlesPresenter;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.views.ArticleListView;
import in.mobileappdev.news.views.ErrorBuilder;
import in.mobileappdev.news.views.ErrorClickListener;

public class ArticlesActivity extends AppCompatActivity implements
        NewsArticleslistAdapter.OnClickListener, ArticleListView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.articlesRecyclerView)
    RecyclerView articlesRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    private String TAG = "ArticlesActivity";
    private ArrayList<Article> listOfArticles = new ArrayList<>();
    private NewsArticlesPresenter presenter;
    private NewsArticleslistAdapter newsArticlesAdapter;
    private ErrorBuilder errorBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent getIntent = getIntent();
        if (null == getIntent) {
            Toast.makeText(this, getResources().getString(R.string.error_something_wrong), Toast.LENGTH_LONG)
                    .show();
            finish();
            return;
        }

        String sourceId = getIntent.getStringExtra(Constants.SOURCE_ID);
        String sourceName = getIntent.getStringExtra(Constants.SOURCE_NAME);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(sourceName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (sourceId == null) {
            Toast.makeText(this, "Something Wrong happened, Please select again", Toast.LENGTH_LONG)
                    .show();
            finish();
            return;
        }

        errorBuilder = new ErrorBuilder(this, errorLayout, new ErrorClickListener() {
            @Override
            public void onRetryClicked(View view) {
                if (presenter != null) {
                    presenter.destroy();
                    presenter.start();
                }
            }
        });

        presenter = new NewsArticlesPresenter(this, sourceId);
        presenter.start();

        initUi();
    }

    private void initUi() {
        newsArticlesAdapter = new NewsArticleslistAdapter(this, listOfArticles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        articlesRecyclerView.setLayoutManager(layoutManager);
        articlesRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation()));
        articlesRecyclerView.setAdapter(newsArticlesAdapter);
        newsArticlesAdapter.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view, int position) {
        //View newsimageView = view.findViewById(R.id.thumbnail);
        NewsDetailActivity.launch(this, position);
        Log.d(TAG, "EVENT BUS onClick " + listOfArticles.size());
        EventBus.getDefault().postSticky(new ArticleListEvent(listOfArticles));

    }


    @Override
    public void showLoading() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoading() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void showArticles(List<Article> articles) {
        listOfArticles.clear();
        listOfArticles.addAll(articles);
        newsArticlesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message, int type) {
        hideLoading();
        errorLayout.setVisibility(View.VISIBLE);
        errorBuilder.showError(message, 0, true);
    }

    @Override
    public void hideError() {
        errorLayout.setVisibility(View.GONE);

    }

    @Override
    public void onRefresh() {
        if (presenter != null) {
            presenter.destroy();
            presenter.start();
        }
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
