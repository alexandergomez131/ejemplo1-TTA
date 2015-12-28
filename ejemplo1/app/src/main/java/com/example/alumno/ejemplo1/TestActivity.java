package com.example.alumno.ejemplo1;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textWording = (TextView)findViewById(R.id.test_wording);
        textWording.setText(R.string.testQuestion1);
        RadioGroup textChoices = (RadioGroup)findViewById(R.id.test_choices);

        RadioButton[] choices = new RadioButton[5];
        String[] choicesString = new String[5];
        for(int i=0; i<5; i++)
            choicesString[i] = new String();

        choicesString=fillChoices(choicesString);

        for(int i=0; i<5; i++) {
            choices[i] = new RadioButton(this);
            choices[i].setText(choicesString[i]);
            choices[i].setOnClickListener(this);
            textChoices.addView(choices[i]);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public String[] fillChoices(String[] choicesString){
        choicesString[0] = getResources().getString(R.string.testAnswer1y1);
        choicesString[1] = getResources().getString(R.string.testAnswer1y2);
        choicesString[2] = getResources().getString(R.string.testAnswer1y3);
        choicesString[3] = getResources().getString(R.string.testAnswer1y4);
        choicesString[4] = getResources().getString(R.string.testAnswer1y5);

        return choicesString;
    }


    @Override
    public void onClick(View v) {
        findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
    }

    public void send(View v){
        RadioGroup textChoices = (RadioGroup)findViewById(R.id.test_choices);
        for(int i=0; i<5; i++)
            textChoices.getChildAt(i).setEnabled(false);
        findViewById(R.id.button_send_test).setVisibility(View.INVISIBLE);

        textChoices.getChildAt(2).setBackgroundColor(Color.GREEN);
        int selected = textChoices.indexOfChild(textChoices.findViewById(textChoices.getCheckedRadioButtonId()));
        if(selected==2)
            Toast.makeText(getApplicationContext(),R.string.correct,Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(getApplicationContext(), R.string.incorrect, Toast.LENGTH_SHORT).show();
            findViewById(R.id.button_help_test).setVisibility(View.VISIBLE);
            textChoices.getChildAt(selected).setBackgroundColor(Color.RED);
        }
    }
}
