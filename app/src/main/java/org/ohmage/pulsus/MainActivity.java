package org.ohmage.pulsus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.smalldatalab.omhclient.DSUClient;

public class MainActivity extends AppCompatActivity {

    Button squareTaskButton, balloonGameButton, selfReportButton;
    private DSUClient mDSUClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init a DSU client
        mDSUClient =
                new DSUClient(
                        DSUHelper.getUrl(this),
                        this.getString(R.string.dsu_client_id),
                        this.getString(R.string.dsu_client_secret),
                        this);

        squareTaskButton = (Button) findViewById(R.id.square_task_button);
        squareTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SquareTask.class);
                startActivity(intent);
            }
        });

        balloonGameButton = (Button) findViewById(R.id.balloon_game_button);
        balloonGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BalloonGame.class);
                startActivity(intent);
            }
        });

        selfReportButton = (Button) findViewById(R.id.self_report_button);
        selfReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelfReport.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // show LoginActivity if the user has not sign in
        if (!mDSUClient.isSignedIn()) {
            Intent mainActivityIntent = new Intent(MainActivity.this, SigninActivity.class);
            startActivity(mainActivityIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logout:
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            mDSUClient.blockingSignOut();
                        } catch (Exception e) {
                            Log.e(MainActivity.class.getSimpleName(), "Logout error", e);
                        }
                        Intent mainActivityIntent = new Intent(MainActivity.this, SigninActivity.class);
                        startActivity(mainActivityIntent);
                    }
                }.start();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
