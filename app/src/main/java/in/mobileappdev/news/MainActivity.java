package in.mobileappdev.news;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import in.mobileappdev.news.adapters.NewsSourcesGridAdapter;
import in.mobileappdev.news.api.APIClient;
import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.models.SourcesResponce;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private NewsSourcesGridAdapter newsSourcesGridAdapter;
  private ArrayList<Source> newsSourcesList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    RecyclerView newsSorcesRecyclerView = (RecyclerView) findViewById(R.id.sources_recycler);
    newsSourcesGridAdapter = new NewsSourcesGridAdapter(this, newsSourcesList);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
    newsSorcesRecyclerView.setLayoutManager(layoutManager);
    newsSorcesRecyclerView.setAdapter(newsSourcesGridAdapter);
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
            Log.d(TAG, "In onError() "+e.getMessage());
          }

          @Override public void onNext(SourcesResponce sources) {
            Log.d(TAG, "In onNext()"  +sources.getSources().size());
            //adapter.setGitHubRepos(gitHubRepos);
            if(sources.getSources().size()>0){
              newsSourcesList.clear();
              newsSourcesList.addAll(sources.getSources());
              newsSourcesGridAdapter.notifyDataSetChanged();
            }
          }
        });
  }
}
