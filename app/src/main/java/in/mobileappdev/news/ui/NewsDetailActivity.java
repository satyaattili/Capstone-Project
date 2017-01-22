package in.mobileappdev.news.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsDetailViewPagerAdapter;
import in.mobileappdev.news.app.NewsApp;
import in.mobileappdev.news.bus.ArticleListEvent;
import in.mobileappdev.news.models.Article;

public class NewsDetailActivity extends AppCompatActivity {

    @BindView(R.id.news_detail_pager)
    ViewPager newsDdetailViewPager;
    FragmentManager fm = getSupportFragmentManager();
    //@BindView(R.id.toolbar) Toolbar toolbar;
    private String TAG = NewsDetailActivity.class.getSimpleName();
    private NewsDetailViewPagerAdapter adapter;
    private int articlePosition = 0;
    private List<Fragment> fragmentList = new ArrayList<>();

    public static void launch(Activity activity, int position) {
        activity.startActivity(getLaunchIntent(activity, position));
    }

    public static Intent getLaunchIntent(Context context, int position) {

        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("position", position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        onNewIntent(getIntent());
        if (NewsApp.getAppInstance().getFirstLaunch()) {
            NewsApp.getAppInstance().setFirstLaunch(false);
            NewsDetailHelperFragment helperFragment = new NewsDetailHelperFragment();
            helperFragment.show(fm, "helper");
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void recieveArticleList(ArticleListEvent articleListEvent) {
        Log.d(TAG, "EVENT BUS recieveArticleList " + articleListEvent.getArticleList().size());
        fragmentList.clear();
        for (Article article : articleListEvent.getArticleList()) {
            fragmentList.add(NewsDetailActivityFragment.createInstance(article));
        }
        adapter = new NewsDetailViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        newsDdetailViewPager.setAdapter(adapter);
        newsDdetailViewPager.setCurrentItem(articlePosition);

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            articlePosition = intent.getIntExtra("position", 0);
        }
    }
}
