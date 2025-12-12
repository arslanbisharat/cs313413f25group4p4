package edu.luc.etl.cs313.android.simplestopwatch.model.state;

import edu.luc.etl.cs313.android.simplestopwatch.R;

class AlarmState implements StopwatchState {

    public AlarmState(final StopwatchSMStateView sm) {
        this.sm = sm;
    }

    private final StopwatchSMStateView sm;

    @Override
    public void onStartStop() {
        sm.actionStopAlarm();
        sm.actionReset();
        sm.toStoppedState();
    }

    @Override
    public void onLapReset() {
        // No action in alarm state for lap/reset
    }

    @Override
    public void onTick() {
        // Alarm continues beeping, do nothing on tick
    }

    @Override
    public void updateView() {
        sm.updateUIRuntime();
    }

    @Override
    public int getId() {
        return R.string.ALARM;
    }
}