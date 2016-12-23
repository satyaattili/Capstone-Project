package in.mobileappdev.news.bus;

import in.mobileappdev.news.models.Article;

/**
 * Created by satyanarayana.avv on 23-12-2016.
 */

public class ArticleListEvent {

  private Article article;

  public ArticleListEvent(Article article) {
    this.article = article;
  }

  public Article getArticle() {
    return article;
  }

  public void setArticle(Article article) {
    this.article = article;
  }
}
