package org.ohmage.pulsus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by anasb on 19/05/16.
 */
class Adapter extends BaseAdapter {

    Context context;
    String[] questions;
    int[] answers;
    private static LayoutInflater inflater = null;

    public Adapter(Context context, String[] questions) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.questions = questions;

        // Initialize answers at 5 (middle value)
        this.answers = new int[questions.length];
        for (int i=0; i<answers.length; i++) {
            answers[i] = 5;
        }

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return questions.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return questions[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public int[] getAnswers() {
        return answers;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Row
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        // Question
        TextView questionTextView = (TextView) vi.findViewById(R.id.question_textview);
        questionTextView.setText(questions[position]);

        // Value Indicator
        final TextView valueIndicator = (TextView) vi.findViewById(R.id.value_indicator);

        // SeekBar
        SeekBar seekBar = (SeekBar) vi.findViewById(R.id.slider);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                answers[position] = progress;
                valueIndicator.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        return vi;
    }
}