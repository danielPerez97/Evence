package teamevence.evenceapp.qrcameraview

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidTest
import teamevence.evenceapp.qrcameraview.ui.QrReaderActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ExampleInstrumentedTest
{
    @get:Rule
    val activityRule = ActivityScenarioRule(QrReaderActivity::class.java)


    @Test
    fun TestQR()
    {

    }

    @Test
    fun useAppContext()
    {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("teamevence.evenceapp.qrcameraview.test", appContext.packageName)
    }
}