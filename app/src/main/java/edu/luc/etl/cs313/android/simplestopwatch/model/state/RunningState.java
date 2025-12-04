package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class RunningState implements StopwatchState {

    public RunningState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {
        sm.actionStop();
        // Reset Time and go to Stopped State
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onTick() {
        sm.actionDec();
        // If time reaches 0, go to Alarm State
        if (sm.getRuntime() == 0) {
            sm.actionStartAlarm();
            sm.toAlarmState();
        }
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.RUNNING;
    }
}
