package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedState implements StopwatchState {

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    private int idleTickCount = 0;
    private static final int IDLE_TIMEOUT = 3; // 3 seconds
    private static final int MAX_TIME = 99; // 99 seconds
    private boolean idleArmed = false;

    @Override
    public void onStartStop() {
        // Increment timer value
        sm.actionInc();
        // Reset idle counter when button is pressed
        idleTickCount = 0;

        if (sm.getRuntime() >= MAX_TIME) {
            sm.actionBeep();
            sm.actionStart();
            sm.toRunningState();
        } else if (sm.getRuntime() > 0) {
            idleArmed = true;
            // the clock  runs in background
            // so that the idle countdown is measured only by onTick events
        }
    }

    @Override
    public void onTick() {
        // counts idle time if countdown was armed by a recent button press.
        if (!idleArmed) {
            return;
        }

        if (sm.getRuntime() <= 0) {
            idleTickCount = 0;
            idleArmed = false;
            return;
        }

        idleTickCount++;
        if (idleTickCount >= IDLE_TIMEOUT) {
            // beep and start after IDLE_TIMEOUT seconds
            idleTickCount = 0;
            idleArmed = false;
            sm.actionBeep();
            sm.actionStart();
            sm.toRunningState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.STOPPED;
    }
}
