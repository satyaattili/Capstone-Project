package in.mobileappdev.news.bus;

import java.util.List;

import in.mobileappdev.news.models.Article;

/**
 * Created by satyanarayana.avv on 23-12-2016.
 */

public class ArticleListEvent {

    private List<Article> articles;

    public ArticleListEvent(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticleList() {
        return articles;
    }

    public void setArticle(List<Article> article) {
        this.articles = articles;
    }
}
