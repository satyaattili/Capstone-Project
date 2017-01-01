package in.mobileappdev.news.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.utils.Constants;

public class NewsDetailWebActivity extends AppCompatActivity
    implements SwipeRefreshLayout.OnRefreshListener {

  private String TAG = "NewsDetailWebActivity";
  private boolean isDeeplink = false;
  @BindView(R.id.webview)
  WebView webView;

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_detail_web);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    onNewIntent(getIntent());
  }

  private void setToolbarTitle(String url) {
    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(url);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
  }

  private void loadUrl(String url) {
    webView.getSettings().setJavaScriptEnabled(true);
    webView.setWebViewClient(new CustomWebViewclient(this));
    webView.loadUrl(url);
  }


  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    String action = intent.getAction();
    String url;
    Uri data = intent.getData();
    if (Intent.ACTION_VIEW.equals(action) && data != null) {
      url = data.getQueryParameter(Constants.DEEPLINK);
      isDeeplink = true;
    } else {
      url = intent.getStringExtra(Constants.URL);
      isDeeplink = false;
    }
    setToolbarTitle(url);
    loadUrl(url);
  }

  @Override
  public void onRefresh() {

  }

  private class CustomWebViewclient extends WebViewClient {
    private CustomWebViewclient(Context context) {
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
      super.onReceivedError(view, request, error);
    }
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      backPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  private void backPressed() {
    if(isDeeplink){
      startActivity(new Intent(this, MainActivity.class));
      finish();
    }else {
      super.onBackPressed();
    }
  }

  @Override
  public void onBackPressed() {
    backPressed();
  }
}
