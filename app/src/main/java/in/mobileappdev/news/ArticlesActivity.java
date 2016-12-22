package in.mobileappdev.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.adapters.NewsArticlesListAdapter;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.presenter.NewsArticlesPresenter;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.views.ArticleListView;

public class ArticlesActivity extends AppCompatActivity implements
    NewsArticlesListAdapter.OnClickListener, ArticleListView, SwipeRefreshLayout.OnRefreshListener {

  private String TAG = "ArticlesActivity";

  private ArrayList<Article> listOfArticles = new ArrayList<>();
  private NewsArticlesPresenter presenter;
  private NewsArticlesListAdapter newsArticlesAdapter;

  @BindView(R.id.articlesRecyclerView)
  RecyclerView articlesRecyclerView;

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_articles);
    ButterKnife.bind(this);

    Intent getIntent = getIntent();
    if (null == getIntent) {
      Toast.makeText(this, "Something Wrong happened, Please select again", Toast.LENGTH_LONG)
          .show();
      finish();
      return;
    }
    String sourceId = getIntent.getStringExtra(Constants.SOURCE_ID);

    if (sourceId == null) {
      Toast.makeText(this, "Something Wrong happened, Please select again", Toast.LENGTH_LONG)
          .show();
      finish();
      return;
    }

    presenter = new NewsArticlesPresenter(this, sourceId);
    presenter.start();

    initUi();
  }

  private void initUi() {
    newsArticlesAdapter = new NewsArticlesListAdapter(this, listOfArticles);
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
  public void onClick(int position) {

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
  public void onRefresh() {
    if (presenter != null) {
      presenter.destroy();
      presenter.start();
    }
  }
}
