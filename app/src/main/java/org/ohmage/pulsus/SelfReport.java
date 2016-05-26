package org.ohmage.pulsus;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import io.smalldatalab.omhclient.DSUDataPoint;
import io.smalldatalab.omhclient.DSUDataPointBuilder;

public class SelfReport extends AppCompatActivity {

    ListView listview;
    Button doneButton;
    TextView header;

    Boolean isBaselineSurvey;

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
            isBaselineSurvey = extras.getBoolean("isBaseline", !hasCompletedBaseline());
        } else {
            isBaselineSurvey = !hasCompletedBaseline();
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
                int[] answers = adapter.getAnswers();
                submit(answers);
                Toast.makeText(SelfReport.this, "Thanks for Completing a Self-Report!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private boolean hasCompletedBaseline() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        return prefs.getBoolean("HAS_COMPLETED_BASELINE", false);
    }

    private void submit(int[] answers)  {

        // Remember test was made
        if (isBaselineSurvey) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            prefs.edit().putBoolean("HAS_COMPLETED_BASELINE", true).commit();
        }

        try {
            DateTime dt = new DateTime();

            JSONObject obj = new JSONObject();
            String[] questions = getSurveyQuestions();
            for (int i = 0; i < answers.length; i++) {
                obj.put(questions[i], answers[i]);
            }

            String testType = isBaselineSurvey ? "baseline" : "daily";
            obj.put("test_type", testType);

            DSUDataPoint dataPoint = new DSUDataPointBuilder()
                    .setSchemaNamespace(getString(R.string.schema_namespace))
                    .setSchemaName(getString(R.string.vas_schema_name))
                    .setSchemaVersion(getString(R.string.schema_version))
                    .setAcquisitionModality(getString(R.string.acquisition_modality))
                    .setAcquisitionSource(getString(R.string.acquisition_source_name))
                    .setCreationDateTime(dt.toGregorianCalendar())
                    .setBody(obj).createDSUDataPoint();
            dataPoint.save();

        } catch (Exception e) {
            Toast.makeText(SelfReport.this, "Submission failed. Please contact study coordinator", Toast.LENGTH_LONG).show();
        }
    }


    private String[] getSurveyQuestions() {
        if (isBaselineSurvey) {
            String[] baselineQuestions = {
                    "I get distracted easily",
                    "I do things that I end up regretting later",
                    "I have difficulty controlling how much I check my mobile phone",
                    "I stick to my long-term goals even if I am tempted by short-term pleasure",
                    "I tend to do things that feel good in the short-term but are bad for me in the long-term",
                    "I have difficulty controlling myself when I am tempted by something even if I don’t want to do it",
                    "I have difficulty controlling how much I use social media",
                    "I feel like I am missing out on fun activities going on around me",
                    "I have difficulty completing tasks that require me to stay focused for long periods",
                    "I tend to do things I regret because I get influenced by other people"
            };
            return baselineQuestions;
        } else {
            String[] dailyQuestions = {"I felt distracted",
                    "I did or said things without thinking",
                    "I felt well rested and alert",
                    "I felt bored with what I was doing"
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
