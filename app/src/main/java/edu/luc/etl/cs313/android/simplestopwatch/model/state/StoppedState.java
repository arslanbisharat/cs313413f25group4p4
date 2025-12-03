package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class StoppedState implements StopwatchState {

    public StoppedState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    private int idleTickCount = 0;
    private static final int IDLE_TIMEOUT = 3; // 3 seconds

    @Override
    public void onStartStop() {
        // Increment timer value
        sm.actionInc();
        // Reset idle counter when button is pressed
        idleTickCount = 0;
        // If max value reached, start immediately
        if (sm.getRuntime() >= 99) {
            sm.actionStart();
            sm.toRunningState();
        }
    }

    @Override
    public void onTick() {
        // If timer has a value set, count down idle time
        if (sm.getRuntime() > 0) {
            idleTickCount++;
            if (idleTickCount >= IDLE_TIMEOUT) {
                // 3 seconds elapsed, beep and start running
                idleTickCount = 0;
                sm.actionBeep();
                sm.actionStart();
                sm.toRunningState();
            }
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
