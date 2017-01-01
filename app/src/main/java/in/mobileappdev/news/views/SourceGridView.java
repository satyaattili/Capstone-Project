package in.mobileappdev.news.views;

import java.util.List;

import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.models.Source;

/**
 * Created by satyanarayana.avv on 22-12-2016.
 */

public interface SourceGridView {

  public void showLoading();

  public void hideLoading();

  public void showSources(List<Source> sources);

  public void showError(String message, int type);

  public void hideError();

}
