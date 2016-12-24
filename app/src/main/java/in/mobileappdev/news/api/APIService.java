package in.mobileappdev.news.api;

import java.util.List;

import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.models.SourcesResponce;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public interface APIService {

  @GET("sources")
  Observable<SourcesResponce> getNewsSources(@Query("language") String language, @Query("country")
      String country);

  //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=fdf9c53649454ac8bedb495857725aae
  @GET("articles")
  Observable<NewsArticlesListResponse> getNewsArticles(@Query("source") String source, @Query
      ("sortBy") String sortBy, @Query("apiKey") String apiKey);

}
