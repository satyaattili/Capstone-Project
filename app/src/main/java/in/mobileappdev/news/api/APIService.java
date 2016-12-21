package in.mobileappdev.news.api;

import java.util.List;

import in.mobileappdev.news.models.SourcesResponce;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

public interface APIService {

  @GET("sources")
  Observable<SourcesResponce> getNewsSources();

}
