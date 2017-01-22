package in.mobileappdev.news.presenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import in.mobileappdev.news.models.Source;
import in.mobileappdev.news.views.SourceGridView;

/**
 * Udacity
 * Created by satyanarayana.avv on 06-01-2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class NewsSourcePresenterTest {

    private SourceGridView gridView = new SourceGridView() {
        @Override
        public void showLoading() {

        }

        @Override
        public void hideLoading() {

        }

        @Override
        public void showSources(List<Source> sources) {

        }

        @Override
        public void showError(String message, int type) {

        }

        @Override
        public void hideError() {

        }
    };

    @Test
    public void testGetSources() {

        NewsSourcesPresenter world = new NewsSourcesPresenter(gridView);
        NewsSourcesPresenter spy = Mockito.spy(world);
        Mockito.doNothing().when(spy).start();
    }
}
