package org.ohmage.pulsus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button completeBaselineButton = (Button) findViewById(R.id.complete_baseline_button);
        completeBaselineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SelfReport.class);
                intent.putExtra("isBaseline",Boolean.TRUE);
                startActivity(intent);
            }
        });
    }
}
