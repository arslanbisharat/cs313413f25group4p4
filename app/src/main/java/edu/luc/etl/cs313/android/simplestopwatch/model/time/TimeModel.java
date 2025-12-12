package edu.luc.etl.cs313.android.simplestopwatch.model.time;

/**
 * The passive data model of the stopwatch.
 * It does not emit any events.
 *
 * @author laufer
 */
public interface TimeModel {
    void resetRuntime();
    void incRuntime();
    // New Decrement Runtime Method
    void decRuntime();
    int getRuntime();
    void setLaptime();
    int getLaptime();
}
