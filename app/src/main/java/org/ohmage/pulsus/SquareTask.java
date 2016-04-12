package org.ohmage.pulsus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SquareTask extends AppCompatActivity {

    Button startTaskButton;
    TextView instructionsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_task);

        findViews();
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
    }

    private void startSquareTask() {
        // Hide instructions and button
        instructionsTextView.animate().alpha(0.0f);
        startTaskButton.animate().alpha(0.0f);
    }
}
