package in.mobileappdev.news.ui;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import in.mobileappdev.news.R;
import in.mobileappdev.news.RobolectricGradleTestRunner;

import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Udacity
 * Created by satyanarayana.avv on 06-01-2017.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(manifest = Config.NONE)
public class SourceActivityTest {

    @Test
    public void shouldFail() {
        assertTrue(true);
    }

}
