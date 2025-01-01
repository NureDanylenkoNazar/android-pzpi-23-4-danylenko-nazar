package danylenko.nazar.nure;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView counterTextView;
    private TextView timerTextView;
    private Button incrementButton;

    private int counter = 0;

    private long startTime = 0L;
    private long pausedTime = 0L;
    private Handler timerHandler = new Handler();
    private boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        counterTextView = findViewById(R.id.counter_text);
        timerTextView = findViewById(R.id.timer_text);
        incrementButton = findViewById(R.id.increment_button);

        incrementButton.setOnClickListener(view -> {
            counter++;
            counterTextView.setText(String.valueOf(counter));
        });

        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt("counter", 0);
            pausedTime = savedInstanceState.getLong("pausedTime", 0L);
            counterTextView.setText(String.valueOf(counter));
        }

        startTimer();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("counter", counter);
        outState.putLong("pausedTime", pausedTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        if (!isTimerRunning) {
            startTime = SystemClock.uptimeMillis() - pausedTime;
            timerHandler.post(timerRunnable);
            isTimerRunning = true;
        }
    }

    private void stopTimer() {
        if (isTimerRunning) {
            pausedTime = SystemClock.uptimeMillis() - startTime;
            timerHandler.removeCallbacks(timerRunnable);
            isTimerRunning = false;
        }
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long elapsedMillis = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (elapsedMillis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            String time = String.format("%02d:%02d", minutes, seconds);
            timerTextView.setText(time);

            timerHandler.postDelayed(this, 1000);
        }
    };
}