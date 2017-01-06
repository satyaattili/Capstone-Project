package in.mobileappdev.news;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import in.mobileappdev.news.utils.Utils;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Udacity
 * Created by satyanarayana.avv on 06-01-2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    private static final String FAKE_STRING = "HELLO WORLD";

    @Mock
    Context mMockContext;

    @Test
    public void getTimeWithInputNull() {
        assertEquals("", Utils.getTimeString(null));
    }

    @Test
    public void getTimeWithInputnvalidFormat() {
        assertEquals("", Utils.getTimeString("sdsfddfdfdg"));
    }

    @Test
    public void getTimeWithInputEmptyString() {
        assertEquals("", Utils.getTimeString(""));
    }

    @Test
    public void isEmptyTestWithNull() {
        assertTrue(Utils.isEmpty(null));
    }

    @Test
    public void isEmptyTestWithEmpty() {
        assertTrue(Utils.isEmpty(""));
    }

    @Test
    public void isEmptyTestWithValid() {
        assertFalse(Utils.isEmpty("Android"));
    }


}
