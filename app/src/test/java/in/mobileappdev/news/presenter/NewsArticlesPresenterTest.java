package in.mobileappdev.news.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.views.ArticleListView;
import in.mobileappdev.news.views.SourceGridView;

/**
 * Udacity
 * Created by satyanarayana.avv on 06-01-2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class NewsArticlesPresenterTest {

    @Test
    public void testGetSources() {

        NewsArticlesPresenter world = new NewsArticlesPresenter(articleListView, "cnn");
        NewsArticlesPresenter spy = Mockito.spy(world);
        Mockito.doNothing().when(spy).start();
    }


    private ArticleListView articleListView = new ArticleListView() {
        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void showArticles(List<Article> articles) {

        }

        @Override
        public void showError(String message, int type) {

        }

        @Override
        public void hideError() {

        }
    };
}
