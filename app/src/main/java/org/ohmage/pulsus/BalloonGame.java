package org.ohmage.pulsus;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import tyrantgit.explosionfield.ExplosionAnimator;
import tyrantgit.explosionfield.ExplosionField;

public class BalloonGame extends AppCompatActivity {

    // Constants
    final float SCALE_ORIGINAL = 0.2f;
    final float SCALE_X = 0.1f;
    final float SCALE_Y = 0.08f;
    final int MAX_PUMPS = 12;
    final int numberOfBalloons = 15;
    final float gainsPerPump = 0.25f;

    // UI
    ImageView balloonImageView;
    Button pumpButton;
    Button collectButton;
    RelativeLayout instructionsLayout;
    RelativeLayout gameLayout;
    Button startButton;

    // Test Variables
    private ExplosionField mExplosionField;
    int pumps = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balloon_game);

        findViews();

        // Prevent pumping or collecting before game start
        pumpButton.setEnabled(false);
        collectButton.setEnabled(false);

        // Hide Instructions when Start is clicked
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Hide instructions and show game layout
                instructionsLayout.animate().setDuration(300).alpha(0.f);
                gameLayout.animate().setDuration(300).alpha(1.f);
                gameLayout.bringToFront();

                // Allow for pump and collect
                pumpButton.setEnabled(true);
                collectButton.setEnabled(true);
            }
        });
        mExplosionField = ExplosionField.attach2Window(this);

        pumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tappedPump();
            }
        });

        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tappedCollect();
            }
        });
    }

    private void findViews() {
        instructionsLayout = (RelativeLayout) findViewById(R.id.balloon_game_instructions);
        gameLayout = (RelativeLayout) findViewById(R.id.balloon_game_layout);
        startButton = (Button) findViewById(R.id.start_balloon_game_button);

        balloonImageView = (ImageView) findViewById(R.id.balloon_image_view);
        pumpButton = (Button) findViewById(R.id.pump_button);
        collectButton = (Button) findViewById(R.id.collect_button);
    }

    private void resetBalloon() {
        balloonImageView.setScaleX(SCALE_ORIGINAL);
        balloonImageView.setScaleY(SCALE_ORIGINAL);
        balloonImageView.setAlpha(1.f);
        mExplosionField.clear();
    }

    private void tappedPump() {

        if (pumps < MAX_PUMPS) {
            balloonImageView.animate().scaleXBy(SCALE_X);
            balloonImageView.animate().scaleYBy(SCALE_Y);
            pumps++;

        } else {
            mExplosionField.explode(balloonImageView);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetBalloon();
                }
            }, 1300);
            pumps = 0;
        }
    }

    private void tappedCollect() {
        pumps = 0;
        balloonImageView.setScaleX(SCALE_ORIGINAL);
        balloonImageView.setScaleY(SCALE_ORIGINAL);
    }
}
