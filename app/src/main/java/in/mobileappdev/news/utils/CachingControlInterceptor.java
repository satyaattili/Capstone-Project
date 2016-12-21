package in.mobileappdev.news.utils;

import java.io.IOException;

import in.mobileappdev.news.app.NewsApp;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public class CachingControlInterceptor implements Interceptor {
  @Override
  public Response intercept(Interceptor.Chain chain) throws IOException {
    Request request = chain.request();

    // Add Cache Control only for GET methods
    if (request.method().equals("GET")) {
      if (Utils.inNetworkConnected()) {
        // 1 day
        request = request.newBuilder()
            .header("Cache-Control", "only-if-cached")
            .build();
      } else {
        // 4 weeks stale
        request = request.newBuilder()
            .header("Cache-Control", "public, max-stale=2419200")
            .build();
      }
    }

    Response originalResponse = chain.proceed(request);
    return originalResponse.newBuilder()
        .header("Cache-Control", "max-age=600")
        .build();
  }
}