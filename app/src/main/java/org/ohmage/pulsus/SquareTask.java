package org.ohmage.pulsus;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;

public class SquareTask extends AppCompatActivity {

    // Layout
    Button startTaskButton;
    TextView instructionsTextView;
    View squareView;
    View plusSign;
    RelativeLayout mainLayout;
    TextView feedbackLabel;

    // Flags
    Boolean testInProgress = Boolean.FALSE;
    Boolean shouldTap = Boolean.FALSE;

    Chronometer chronometer;
    int TOTAL_LOOPS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_task);

        findViews();

        // OnClick Listeners
        startTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSquareTask();
            }
        });
    }

    private void findViews() {
        startTaskButton = (Button) findViewById(R.id.start_square_task_button);
        instructionsTextView = (TextView) findViewById(R.id.square_task_instructions);
        squareView = findViewById(R.id.game_view);
        plusSign = findViewById(R.id.plus_image);
        mainLayout = (RelativeLayout) findViewById(R.id.square_task_main_layout);
        feedbackLabel = (TextView) findViewById(R.id.feedback_label);
    }

    private void startSquareTask() {
        // Hide instructions
        instructionsTextView.animate().alpha(0.0f);

        // Hide Start Button
        startTaskButton.animate().alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {

                mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tappedSquare();
                    }
                });

                chronometer = new Chronometer(getBaseContext());
                loop();
            }
        });
    }

    private void loop() {
        // Show plus sign for 800 ms
        plusSign.setAlpha(1.0f);

        // Hide plus sign
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                plusSign.setAlpha(0.0f);

                // Show empty square after 500ms
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        squareView.setAlpha(1.0f);
                        squareView.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.no_color_background));

                        // Then show color after 550ms
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                // Randomly select between valid and invalid color
                                int i = new Random().nextInt(100 + 1);
                                int drawable;
                                if (i > 30) {
                                    drawable = R.drawable.valid_color_background;
                                    shouldTap = Boolean.TRUE;
                                } else {
                                    drawable = R.drawable.invalid_color_background;
                                    shouldTap = Boolean.FALSE;
                                }
                                squareView.setBackground(ContextCompat.getDrawable(getBaseContext(), drawable));

                                // Allow for tapping screen
                                testInProgress = Boolean.TRUE;

                                // Start chronometer
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                chronometer.start();

                                // Finally, hide after 1000ms
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        squareView.setAlpha(0.0f);
                                        testInProgress = Boolean.FALSE;

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (--TOTAL_LOOPS > 0) {
                                                    loop();
                                                } else {
                                                    mainLayout.setOnClickListener(null);
                                                    Toast.makeText(getBaseContext(), "Done with 10 loops!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }, 600);
                                    }
                                }, 1000);
                            }
                        }, 550);
                    }
                }, 500);
            }
        }, 800);
    }

    private void tappedSquare() {

        Log.d("TAG", "No test in progress, stop tapping!");

        // No test going on now, ignore taps
        if (!testInProgress) {
            return;
        }

        // Show feedback
        if (shouldTap) {
            long time = SystemClock.elapsedRealtime() - chronometer.getBase();

            feedbackLabel.setText("Correct! "+time+" ms");
            feedbackLabel.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.validColor));
        } else {

            feedbackLabel.setText("Incorrect!");
            feedbackLabel.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.invalidColor));
        }

        // Show feedback label
        feedbackLabel.animate().alpha(1.f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                feedbackLabel.animate().alpha(0.f);
            }
        }, 600);

        chronometer.stop();
    }

    private void showResults() {

    }
}
