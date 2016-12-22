package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsSourcesGridAdapter;
import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.models.SourcesResponce;
import in.mobileappdev.news.utils.Constants;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
    NewsSourcesGridAdapter.OnClickListener{

  private static final String TAG = "MainActivity";
  private NewsSourcesGridAdapter newsSourcesGridAdapter;
  private ArrayList<Source> newsSourcesList = new ArrayList<>();

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.sources_recycler) RecyclerView newsSorcesRecyclerView;
  @BindView(R.id.loadingProgress) ProgressBar loadingProgress;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    newsSourcesGridAdapter = new NewsSourcesGridAdapter(this, newsSourcesList);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
    newsSorcesRecyclerView.setLayoutManager(layoutManager);
    newsSorcesRecyclerView.setAdapter(newsSourcesGridAdapter);
    newsSourcesGridAdapter.setOnClickListener(this);
    getNewsSources("en");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override protected void onDestroy() {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
    super.onDestroy();
  }

  private Subscription subscription;

  private void getNewsSources(String username) {
    loadingProgress.setVisibility(View.VISIBLE);
    subscription = APIClient.getInstance()
        .getNewsSources(username)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<SourcesResponce>() {
          @Override public void onCompleted() {
            Log.d(TAG, "In onCompleted()");
          }

          @Override public void onError(Throwable e) {
            e.printStackTrace();
            loadingProgress.setVisibility(View.GONE);
            Log.d(TAG, "In onError() "+e.getMessage());
          }

          @Override public void onNext(SourcesResponce sources) {
            Log.d(TAG, "In onNext()"  +sources.getSources().size());
            //adapter.setGitHubRepos(gitHubRepos);
            if(sources.getSources().size()>0){
              newsSourcesList.clear();
              newsSourcesList.addAll(sources.getSources());
              loadingProgress.setVisibility(View.GONE);
              newsSourcesGridAdapter.notifyDataSetChanged();
            }
          }
        });
  }

  @Override
  public void onClick(int position) {
    String sourceId = newsSourcesList.get(position).getId();
    Intent articleList = new Intent(MainActivity.this, ArticlesActivity.class);
    articleList.putExtra(Constants.SOURCE_ID, sourceId);
    articleList.putExtra(Constants.SOURCE_NAME, newsSourcesList.get(position).getName());
    startActivity(articleList);
  }
}
