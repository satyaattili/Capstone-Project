package in.mobileappdev.news.api;

import java.util.List;

import in.mobileappdev.news.models.NewsArticlesListResponse;
import in.mobileappdev.news.models.SourcesResponce;
import in.mobileappdev.news.models.TokenResponse;
import in.mobileappdev.news.utils.Constants;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public interface APIService {

  @GET("sources")
  Observable<SourcesResponce> getNewsSources(@Query(Constants.KEY_LANGUAGE) String language, @Query(Constants.KEY_COUNTRY)
      String country);

  //https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=fdf9c53649454ac8bedb495857725aae
  @GET("articles")
  Observable<NewsArticlesListResponse> getNewsArticles(@Query(Constants.KEY_SOURCE) String source, @Query
                  (Constants.KEY_SORT_BY) String sortBy, @Query(Constants.KEY_API_KEY) String apiKey);

  @POST("http://192.168.1.5/fcmserver/register.php")
  @FormUrlEncoded
  Observable<TokenResponse> saveToken(
          @Field(Constants.KEY_NAME) String name,
          @Field(Constants.KEY_EMAIL) String email,
          @Field(Constants.KEY_FCM_ID) String token);

}
