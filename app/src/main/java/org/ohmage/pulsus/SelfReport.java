package org.ohmage.pulsus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class SelfReport extends AppCompatActivity {

    ListView listview;
    Button doneButton;
    TextView header;

    Boolean isBaselineSurvey = Boolean.FALSE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_report);

        // Get Views
        listview = (ListView) findViewById(R.id.listview);
        doneButton = (Button) findViewById(R.id.done_button);
        header = (TextView) findViewById(R.id.header_textview);

        // Retrieve Intent flag about survey type
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isBaselineSurvey = extras.getBoolean("isBaseline", Boolean.FALSE);
        }

        // Header
        header.setText(getHeaderText());

        // Configure ListView
        listview.setAdapter(new Adapter(this, getSurveyQuestions()));

        // Submit Button
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Adapter adapter = (Adapter) listview.getAdapter();
                Toast.makeText(getBaseContext(), "Answers:\n"+Arrays.toString(adapter.getAnswers()), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }


    private String[] getSurveyQuestions() {
        if (isBaselineSurvey) {
            String[] baselineQuestions = {
                    "I get distracted easily.",
                    "I do things that I end up regretting later.",
                    "I have difficulty controlling how much I check my mobile phone.",
                    "I stick to my long-term goals even if I am tempted by short-term pleasure.",
                    "I tend to do things that feel good in the short-term but are bad for me in the long-term.",
                    "I have difficulty controlling myself when I am tempted by something even if I don’t want to do it.",
                    "I have difficulty controlling how much I use social media.",
                    "I feel like I am missing out on fun activities going on around me.",
                    "I have difficulty completing tasks that require me to stay focused for long periods.",
                    "I tend to do things I regret because I get influenced by other people."
            };
            return baselineQuestions;
        } else {
            String[] dailyQuestions = {"I felt distracted.",
                    "I did or said things without thinking.",
                    "I felt well rested and alert.",
                    "I felt bored with what I was doing."
            };
            return dailyQuestions;
        }
    }

    private String getHeaderText() {
        if (isBaselineSurvey) {
            return "Please answer the questions below using the slider that goes from 0 (Not at All like you) to 10 (Extremely like you). Each slider is set to moderately to begin.";
        } else {
            return "Please answer the questions below about your day so far using the slider that goes from 0 (Not at all) to 10 (Extremely)";
        }
    }
}
