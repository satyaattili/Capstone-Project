package in.mobileappdev.news.views;

import java.util.ArrayList;
import java.util.List;

import in.mobileappdev.news.models.Article;

/**
 * Created by satyanarayana.avv on 22-12-2016.
 */

public interface ArticleListView {

  public void showLoading();

  public void hideLoading();

  public void showArticles(List<Article> articles);

}
