package in.mobileappdev.news.api;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import in.mobileappdev.news.app.NewsApp;
import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.models.SourcesResponce;
import in.mobileappdev.news.utils.RxErrorHandlingCallAdapterFactory;
import in.mobileappdev.news.utils.Utils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class APIClient {

  private static final String BASE_URL = "https://newsapi.org/v1/";
  private static final String CACHE_CONTROL = "Cache-Control";


  private static APIClient instance;
  private APIService apiService;

  private APIClient() {

    final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
      //  .client(provideOkHttpClient())
        .build();
    apiService = retrofit.create(APIService.class);
  }

  public static APIClient getInstance() {
    if (instance == null) {
      instance = new APIClient();
    }
    return instance;
  }

  public Observable<SourcesResponce> getNewsSources(@NonNull String language, @NonNull String
      country) {
    return apiService.getNewsSources(language,country);
  }

  public Observable<NewsArticlesListResponse> getArticles(@NonNull String sourceId, String
      sortBy, String apiKey) {
    return apiService.getNewsArticles(sourceId, sortBy, apiKey);
  }


  private static OkHttpClient provideOkHttpClient ()
  {
    return new OkHttpClient.Builder()
        .addInterceptor( provideOfflineCacheInterceptor() )
        .addNetworkInterceptor( provideCacheInterceptor() )
        .cache( provideCache() )
        .build();
  }

  private static Cache provideCache ()
  {
    Cache cache = null;
    try
    {
      cache = new Cache( new File( NewsApp.getAppInstance().getCacheDir(), "http-cache" ),
          10 * 1024 * 1024 ); // 10 MB
    }
    catch (Exception e)
    {
      //Log.e( TAG, "Could not create Cache!" );
    }
    return cache;
  }

  public static Interceptor provideCacheInterceptor ()
  {
    return new Interceptor()
    {
      @Override
      public Response intercept (Chain chain) throws IOException
      {
        Response response = chain.proceed( chain.request() );

        // re-write response header to force use of cache
        CacheControl cacheControl = new CacheControl.Builder()
            .maxAge( 2, TimeUnit.MINUTES )
            .build();

        return response.newBuilder()
            .header( CACHE_CONTROL, cacheControl.toString() )
            .build();
      }
    };
  }

  public static Interceptor provideOfflineCacheInterceptor ()
  {
    return new Interceptor()
    {
      @Override
      public Response intercept (Chain chain) throws IOException
      {
        Request request = chain.request();

        if ( !Utils.inNetworkConnected() )
        {
          CacheControl cacheControl = new CacheControl.Builder()
              .maxStale( 7, TimeUnit.DAYS )
              .build();

          request = request.newBuilder()
              .cacheControl( cacheControl )
              .build();
        }

        return chain.proceed( request );
      }
    };
  }
}