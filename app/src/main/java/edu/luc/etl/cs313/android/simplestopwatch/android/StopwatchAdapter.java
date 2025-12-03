package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.ConcreteStopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;

/**
 * A thin adapter component for the stopwatch.
 *
 * @author laufer
 */
public class StopwatchAdapter extends Activity implements StopwatchModelListener {

    private static String TAG = "stopwatch-android-activity";

    /**
     * The state-based dynamic model.
     */
    private StopwatchModelFacade model;

    /**
     * MediaPlayer for alarm sound.
     */
    private MediaPlayer alarmPlayer;

    protected void setModel(final StopwatchModelFacade model) {
        this.model = model;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject dependency on view so this adapter receives UI events
        setContentView(R.layout.activity_main);
        // inject dependency on model into this so model receives UI events
        this.setModel(new ConcreteStopwatchModelFacade());
        // inject dependency on this into model to register for UI updates
        model.setModelListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model.start();
    }

    // TODO remaining lifecycle methods

    /**
     * Updates the timer display in the UI.
     * @param time
     */
    public void onTimeUpdate(final int time) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView tvS = findViewById(R.id.seconds);
            final var locale = Locale.getDefault();
            tvS.setText(String.format(locale,"%02d", time));
        });
    }

    /**
     * Updates the state name in the UI.
     * @param stateId
     */
    public void onStateUpdate(final int stateId) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView stateName = findViewById(R.id.stateName);
            stateName.setText(getString(stateId));
        });
    }

    // forward event listener methods to the model
    public void onStartStop(final View view) {
        model.onStartStop();
    }

    /**
     * Plays a single beep sound.
     */
    @Override
    public void playBeep() {
        runOnUiThread(() -> {
            try {
                final Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                final MediaPlayer beepPlayer = MediaPlayer.create(getApplicationContext(), notificationUri);
                if (beepPlayer != null) {
                    beepPlayer.start();
                    beepPlayer.setOnCompletionListener(MediaPlayer::release);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Starts the continuous alarm sound.
     */
    @Override
    public void startAlarm() {
        runOnUiThread(() -> {
            try {
                if (alarmPlayer == null) {
                    final Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                    alarmPlayer = new MediaPlayer();
                    alarmPlayer.setDataSource(getApplicationContext(), alarmUri);
                    alarmPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    alarmPlayer.setLooping(true);
                    alarmPlayer.prepare();
                }
                alarmPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Stops the continuous alarm sound.
     */
    @Override
    public void stopAlarm() {
        runOnUiThread(() -> {
            if (alarmPlayer != null && alarmPlayer.isPlaying()) {
                alarmPlayer.stop();
                alarmPlayer.release();
                alarmPlayer = null;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up MediaPlayer
        if (alarmPlayer != null) {
            alarmPlayer.release();
            alarmPlayer = null;
        }
    }
}
