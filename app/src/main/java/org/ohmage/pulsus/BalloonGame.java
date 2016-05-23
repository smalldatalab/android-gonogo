package org.ohmage.pulsus;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import tyrantgit.explosionfield.ExplosionField;

public class BalloonGame extends AppCompatActivity {

    // Constants
    final float SCALE_ORIGINAL = 0.2f;
    final float SCALE_X = 0.1f;
    final float SCALE_Y = 0.08f;
    final int MAX_PUMPS = 12;
    final int NUMBER_OF_BALLOONS = 15;
    final float gainPerPump = 0.25f;

    // UI
    ImageView balloonImageView;
    Button pumpButton;
    Button collectButton;
    RelativeLayout instructionsLayout;
    RelativeLayout gameLayout;
    Button startButton;
    TextView totalEarningsTextView;
    TextView potentialGainTextView;
    TextView instructionsTextView;

    // Test Variables
    private ExplosionField mExplosionField;
    int pumps = 0;
    int currentBalloon = 1;
    int numberOfExplosions = 0;
    float earnings = 0.f;
    float potentialGain = 0.f;

    // Data Recording
    Boolean lastBalloonExploded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balloon_game);

        findViews();

        // Make bold part of the instructions
        TextView instructionsTexView = (TextView) findViewById(R.id.instructions_textview);
        instructionsTexView.setText(Html.fromHtml("Welcome to the Balloon Game.<br/><br/>Your goal for this game is to make as much money as possible by inflating the balloons. To play, tap the <b>pump button to inflate the balloon and earn 25 cents for each pump</b>. To collect your money for each balloon, hit the <b<collect button</b>. But remember, the more you pump the balloon, the greater chance of it bursting. The max amount you can win <b>per balloon</b> is $2.50. When it bursts, you get no money for that balloon. Your goal is to earn as much possible over the <b>15 balloons</b>."));

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

        totalEarningsTextView = (TextView) findViewById(R.id.total_earnings_textview);
        potentialGainTextView = (TextView) findViewById(R.id.potential_gain_textview);
        instructionsTextView = (TextView) findViewById(R.id.instructions_textview);
    }

    private void tappedPump() {

        // Prevent from pumping more than max number of balloons
        if (currentBalloon > NUMBER_OF_BALLOONS) {
            return;
        }

        // Increase pumps
        pumps++;

        // Check whether it should implode now
        if (shouldImplodeNow() || pumps >= MAX_PUMPS) {
            implodeBalloon();
            return;
        }

        // Disable pumping / collecting
        enableButtons(false);

        // Inflate Balloon
        balloonImageView.animate().scaleXBy(SCALE_X);
        balloonImageView.animate().scaleYBy(SCALE_Y).withEndAction(new Runnable() {
            @Override
            public void run() {
                enableButtons(true);
            }
        });

        potentialGain += gainPerPump;
        updateEarningLabels();
    }

    private void tappedCollect() {
        currentBalloon++;
        lastBalloonExploded = false;
        resetBalloon();
    }

    private void implodeBalloon() {

        // Update trackers
        potentialGain = 0;
        currentBalloon++;
        numberOfExplosions++;
        lastBalloonExploded = true;
        updateEarningLabels();

        // Explosion animation
        mExplosionField.explode(balloonImageView);
        enableButtons(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBalloon();
                enableButtons(true);
            }
        }, 1300);
    }

    private Boolean shouldImplodeNow() {
        if (pumps <= 1) {
            return false;
        }

        // Probability of burst: 1-11, 1-10, 1-9, etc.
        int maxValue = (int)(MAX_PUMPS-pumps+1);
        Random r = new Random();
        return (r.nextInt(maxValue) + 1) == 1;
    }

    private void updateEarningLabels() {
        // If some balloons are left, show the count, else only show earnings
        if (currentBalloon <= NUMBER_OF_BALLOONS) {
            String s = "Balloon "+currentBalloon+" out of 15.\nTotal Earnings: $"+ String.format("%.2f", earnings);
            totalEarningsTextView.setText(s);
        } else {
            String s = "Total Earnings:\n$"+ String.format("%.2f", earnings);
            totalEarningsTextView.setText(s);
        }
        String s = "Potential Gain:\n$" + String.format("%.2f", potentialGain);
        potentialGainTextView.setText(s);
    }

    private void enableButtons(Boolean enabled) {
        pumpButton.setEnabled(enabled);
        collectButton.setEnabled(enabled);
    }

    private void resetBalloon() {
        // Reset Balloon Image
        enableButtons(false);
        balloonImageView.animate().scaleX(SCALE_ORIGINAL);
        balloonImageView.animate().scaleY(SCALE_ORIGINAL).withEndAction(new Runnable() {
            @Override
            public void run() {
                enableButtons(true);
            }
        });
        balloonImageView.setAlpha(1.f);
        mExplosionField.clear();

        // Update trackers
        earnings += potentialGain;
        potentialGain = 0;
        pumps = 0;
        updateEarningLabels();

        if (currentBalloon > NUMBER_OF_BALLOONS) {
            showResults();
        }
    }

    private void showResults() {
        // Disable buttons and hide game layout
        enableButtons(false);
        gameLayout.animate().alpha(0.f);
        startButton.setAlpha(0.f);
        instructionsLayout.bringToFront();

        // Results
        String results = "";
        results += "Total Earnings: $" + String.format("%.2f", earnings) +"\n\n";
        results += "Number of Balloon Explosions: "+ numberOfExplosions +"\n\n";
        instructionsTextView.setText(results);
        instructionsLayout.animate().alpha(1.f);
    }
}
