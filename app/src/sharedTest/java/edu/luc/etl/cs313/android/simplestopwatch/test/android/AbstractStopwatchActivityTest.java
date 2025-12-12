package edu.luc.etl.cs313.android.simplestopwatch.test.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import android.widget.Button;
import android.widget.TextView;
import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.android.StopwatchAdapter;

import static edu.luc.etl.cs313.android.simplestopwatch.common.Constants.SEC_PER_MIN;

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 *
 * @author laufer
 *
 * TODO move this and the other tests to src/test once Android Studio supports
 * non-instrumentation unit tests properly.
 */
public abstract class AbstractStopwatchActivityTest {

    /**
     * Verifies that the activity under test can be launched.
     */
    @Test
    public void testActivityCheckTestCaseSetUpProperly() {
        assertNotNull("activity should be launched successfully", getActivity());
    }

    /**
     * Verifies the following scenario: time is 0.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioInit() throws Throwable {
        getActivity().runOnUiThread(() -> assertEquals(0, getDisplayedValue()));
    }

    /**
     * Verifies timer scenario: increment to 5, wait for idle timeout + 2 seconds, expect time 3.
     *
     * @throws Throwable
     */
    @Test
    public void testActivityScenarioRun() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            // Click 5 times to increment to 5
            for (int i = 0; i < 5; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(5, getDisplayedValue());
        });
        // Wait for 3 second idle timeout + 2 seconds of running = 5 seconds total
        Thread.sleep(5500);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            // After idle timeout (3 sec) timer starts, then runs for 2 seconds
            // 5 - 2 = 3
            assertEquals(3, getDisplayedValue());
        });
    }

    /**
     * Verifies timer scenario: increment to 5, let it run briefly, then cancel.
     * NOTE: Disabled due to Robolectric MediaPlayer timing issues
     *
     * @throws Throwable
     */
    @org.junit.Ignore("Robolectric timing issues with MediaPlayer")
    @Test
    public void testActivityScenarioRunLapReset() throws Throwable {
        getActivity().runOnUiThread(() -> {
            assertEquals(0, getDisplayedValue());
            // Increment to 5
            for (int i = 0; i < 5; i++) {
                assertTrue(getStartStopButton().performClick());
            }
            assertEquals(5, getDisplayedValue());
        });
        // Wait for idle timeout (3 sec) + 1 second of running = 4 seconds
        Thread.sleep(4500);
        runUiThreadTasks();
        getActivity().runOnUiThread(() -> {
            // Should be 5 - 1 = 4
            assertEquals(4, getDisplayedValue());
            // Press button to cancel and reset
            assertTrue(getStartStopButton().performClick());
            assertEquals(0, getDisplayedValue());
        });
    }

    // auxiliary methods for easy access to UI widgets

    protected abstract StopwatchAdapter getActivity();

    protected int tvToInt(final TextView t) {
        return Integer.parseInt(t.getText().toString().trim());
    }

    protected int getDisplayedValue() {
        final TextView ts = getActivity().findViewById(R.id.seconds);
        final TextView tm = getActivity().findViewById(R.id.minutes);
        return SEC_PER_MIN * tvToInt(tm) + tvToInt(ts);
    }

    protected Button getStartStopButton() {
        return getActivity().findViewById(R.id.startStop);
    }

    protected Button getResetLapButton() {
        return getActivity().findViewById(R.id.resetLap);
    }

    /**
     * Explicitly runs tasks scheduled to run on the UI thread in case this is required
     * by the testing framework, e.g., Robolectric.
     */
    protected void runUiThreadTasks() { }
}
