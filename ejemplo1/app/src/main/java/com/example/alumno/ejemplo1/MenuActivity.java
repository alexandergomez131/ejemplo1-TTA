package com.example.alumno.ejemplo1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MenuActivity extends AppCompatActivity {

    public static final String EXTRA_AUTH= "com.example.alumno.passwd";

    private final String RestServerURL = "http://u017633.ehu.eus:18080/AlumnoTta/rest/tta";
    private final String RestUser = "getStatus?dni=";
    private String auth;
    private TextView menuLogin;
    private TextView menuLesson;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Intent intent = getIntent();
        menuLogin = (TextView)findViewById(R.id.menu_login);
        menuLesson = (TextView)findViewById(R.id.menu_lesson);
        Toast.makeText(getApplicationContext(),RestUser+intent.getStringExtra(MainActivity.EXTRA_LOGIN) , Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                RestClient client = new RestClient(RestServerURL);
                client.setHttpBasicAuh(intent.getStringExtra(MainActivity.EXTRA_LOGIN), intent.getStringExtra(MainActivity.EXTRA_PASSWD));
                auth=client.getAuthorization();
                try {
                    json = client.getJson(RestUser+intent.getStringExtra(MainActivity.EXTRA_LOGIN));
                    final String name = json.getString("user");
                    final String lessonNumber = json.getString("lessonNumber");
                    final String lessonTitle = json.getString("lessonTitle");

                    menuLogin.post(new Runnable()  {
                        @Override
                        public void run() {
                            String welcome = getResources().getString(R.string.welcomeLabel);
                            menuLogin.setText(welcome + " " + name);
                        }
                    });

                    menuLesson.post(new Runnable() {
                        @Override
                        public void run() {
                            String lesson = getResources().getString(R.string.menuLabel);
                            menuLesson.setText(lesson + " " + lessonNumber + ": " + lessonTitle);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void test(View view){
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra(EXTRA_AUTH,auth);
        startActivity(intent);
    }

}
