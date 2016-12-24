package in.mobileappdev.news.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;

public class NewsDetailWebActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.webview)
  WebView webView;

  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news_detail_web);
    ButterKnife.bind(this);

    Intent intent = getIntent();
    if(intent != null){
      String url = intent.getStringExtra("url");
      webView.getSettings().setJavaScriptEnabled(true);
      webView.setWebViewClient(new CustomWebViewclient(this));
      webView.loadUrl(url);
    }

  }

  @Override
  public void onRefresh() {

  }

  private  class CustomWebViewclient extends WebViewClient {
    private int refreshCount;
    private Context m_context;
    private long m_start;
    private CustomWebViewclient(Context context) {
      m_context = context;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      m_start = System.currentTimeMillis();
      swipeRefreshLayout.setRefreshing(true);
      refreshCount++;
    }
    @Override
    public void onPageFinished(WebView view, String url) {
      long interval = System.currentTimeMillis() - m_start;
      swipeRefreshLayout.setRefreshing(false);
    }
  }
}
