package com.example.alumno.ejemplo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private final String RestServerURL = "http://u017633.ehu.eus:18080/AlumnoTta/rest/tta";
    private final String RestTest = "getTest?id=1";

    private Context contexto = this;
    private LinearLayout layout;
    private TextView textWording;
    private RadioGroup textChoices;
    private int selected;
    private int correct;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        layout=(LinearLayout)findViewById(R.id.test_layout);
        textWording = (TextView)findViewById(R.id.test_wording);
        textChoices = (RadioGroup)findViewById(R.id.test_choices);

        new Thread(new Runnable() {
            @Override
            public void run() {

                RestClient client = new RestClient(RestServerURL);
                client.setAuthorization(intent.getStringExtra(MenuActivity.EXTRA_AUTH));

                try {
                    json = client.getJson(RestTest);
                    textWording.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                textWording.setText(json.getString("wording"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    textChoices.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray respuestas = respuestas = json.getJSONArray("choices");
                                for(int i=0;i<respuestas.length();i++) {

                                    RadioButton choice = new RadioButton(contexto);
                                    choice.setText(respuestas.getJSONObject(i).getString("answer"));
                                    choice.setOnClickListener((View.OnClickListener) contexto);
                                    textChoices.addView(choice);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


      /*  RadioButton[] choices = new RadioButton[5];
        String[] choicesString = new String[5];
        for(int i=0; i<5; i++)
            choicesString[i] = new String();

        choicesString=fillChoices(choicesString);

        for(int i=0; i<5; i++) {
            choices[i] = new RadioButton(this);
            choices[i].setText(choicesString[i]);
            choices[i].setOnClickListener(this);
            textChoices.addView(choices[i]);
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private String[] fillChoices(String[] choicesString){
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
        layout.removeView(findViewById(R.id.button_send_test));

        textChoices.getChildAt(2).setBackgroundColor(Color.GREEN);
        selected = textChoices.indexOfChild(textChoices.findViewById(textChoices.getCheckedRadioButtonId()));
        if(selected==2)
            Toast.makeText(getApplicationContext(),R.string.correct,Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(getApplicationContext(), R.string.incorrect, Toast.LENGTH_SHORT).show();
            findViewById(R.id.button_help_test).setVisibility(View.VISIBLE);
            textChoices.getChildAt(selected).setBackgroundColor(Color.RED);
            //this.selected=selected;
        }
    }

    public void help(View w){
        switch(selected) {
            case 0:
                showHtml("webView");
                break;
            case 1:
                showHtml("external");
                break;
            case 3:
                showVideo();
                break;
            case 4:
                showAudio();
                break;
        }
    }

    private void showHtml(String type){
        if(type=="external") {
            Uri uri = Uri.parse("https://www.youtube.com/watch?v=rDeXSVJy0qU");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if(type=="webView") {
            WebView web = new WebView(this);
            //web.setWebViewClient(new WebViewClient());
            //web.loadUrl("https://www.google.es");
            String html = "<html><body><h2>Hey esto funciona nena!</h2></body></html>";
            web.loadData(html,"text/html",null);
            web.setBackgroundColor(Color.TRANSPARENT);
            web.setLayerType(WebView.LAYER_TYPE_SOFTWARE,null);
            /*LinearLayout layout = (LinearLayout)findViewById(R.id.test_layout);*/
            layout.addView(web);
        }
    }

    private void showVideo(){
        VideoView video = new VideoView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);
        video.setVideoURI(Uri.parse("http://u017633.ehu.eus:18080/static/AndroidManifest.mp4"));

        MediaController controler = new MediaController(this);
        controler.setAnchorView(video);
        video.setMediaController(controler);
        layout.addView(video);
        video.start();
    }

    private void showAudio(){

        LinearLayout audioView = (LinearLayout)findViewById(R.id.audio_view);
        AudioPlayer audio = new AudioPlayer(audioView, new Runnable() {
            public void run() {
                finish();
            }
        });
        audio.setUri();
    }
}
