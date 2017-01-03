package in.mobileappdev.news.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.adapters.NewsSourcesGridAdapter;
import in.mobileappdev.news.app.NewsApp;
import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.presenter.NewsSourcesPresenter;
import in.mobileappdev.news.utils.Constants;
import in.mobileappdev.news.views.ErrorBuilder;
import in.mobileappdev.news.views.ErrorClickListener;
import in.mobileappdev.news.views.SourceGridView;

public class SourcesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NewsSourcesGridAdapter.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        SourceGridView {

    private static final String TAG = "MainActivity";
    private NewsSourcesGridAdapter newsSourcesGridAdapter;
    private ArrayList<Source> newsSourcesList = new ArrayList<>();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sources_recycler)
    RecyclerView newsSorcesRecyclerView;
    @BindView(R.id.loadingProgress)
    ProgressBar loadingProgress;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;
    private NewsSourcesPresenter newsSourcesPresenter;
    private ErrorBuilder errorBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        ButterKnife.bind(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(toolbar);

        if (NewsApp.getAppInstance().getLoginStatus()) {
            initFirebaseAuthentication();
        }

        initUi();

        errorBuilder = new ErrorBuilder(this, errorLayout, new ErrorClickListener() {
            @Override
            public void onRetryClicked(View view) {
                if (newsSourcesPresenter != null) {
                    newsSourcesPresenter.destroy();
                    newsSourcesPresenter.start();
                }
            }
        });

        newsSourcesPresenter = new NewsSourcesPresenter(this);
        newsSourcesPresenter.start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return NewsApp.getAppInstance().getLoginStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            signOut();
            return true;
        }else if(id == android.R.id.home){
            
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFirebaseAuthentication() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    @Override
    protected void onDestroy() {
        if (newsSourcesPresenter != null) {
            newsSourcesPresenter.destroy();
        }
        super.onDestroy();
    }


    private void initUi() {
        newsSourcesGridAdapter = new NewsSourcesGridAdapter(this, newsSourcesList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        newsSorcesRecyclerView.setLayoutManager(layoutManager);
        newsSorcesRecyclerView.setAdapter(newsSourcesGridAdapter);
        newsSourcesGridAdapter.setOnClickListener(this);
    }

    @Override
    public void onClick(int position) {
        String sourceId = newsSourcesList.get(position).getId();
        Intent articleList = new Intent(SourcesActivity.this, ArticlesActivity.class);
        articleList.putExtra(Constants.SOURCE_ID, sourceId);
        articleList.putExtra(Constants.SOURCE_NAME, newsSourcesList.get(position).getName());
        startActivity(articleList);
    }

    private void signOut() {
        if (mAuth != null && mGoogleApiClient != null) {
            mAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            NewsApp.getAppInstance().setLoginStatus(false);
                            invalidateOptionsMenu();
                        }
                    });

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        loadingProgress.setVisibility(View.GONE);
    }

    @Override
    public void showSources(List<Source> sources) {
        newsSourcesList.clear();
        newsSourcesList.addAll(sources);
        newsSourcesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message, int type) {
        errorBuilder.showError(message, type, true);
    }

    @Override
    public void hideError() {
        errorBuilder.hideErrorLayout();
    }
}
