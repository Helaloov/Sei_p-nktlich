package com.example.latlong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



import java.util.Calendar;

public class Activity2 extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Calendar now;
    double Result1;
    int Result;
    double[] Distanz = {1000,1000,1000};
    double[] Zeit = {780,600,300};
    int Total_Distanz_Gehen = 1000;
    int Total_Distanz_Laufen = 1000;
    int Total_Distanz_Radfahren = 1000;
    int Total_Zeit_Gehen = 780;
    int Total_Zeit_Laufen = 600;
    int Total_Zeit_Radfahren = 300;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    public static final  String PREFS_NAME = "egal";

    TextView Datenaktualisierung;

    long tStart;
    long tEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);





        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat(" %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        Datenaktualisierung = (TextView) findViewById(R.id.seco);




        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences settings1 = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);

        Total_Distanz_Gehen = settings.getInt("Total_Distanz_Gehen" , Total_Distanz_Gehen);
        Total_Distanz_Laufen = settings1.getInt("Total_Distanz_Laufen" , Total_Distanz_Laufen);
        Total_Distanz_Radfahren = settings2.getInt("Total_Distanz_Radfahren" , Total_Distanz_Radfahren);
        Distanz[0] = Total_Distanz_Gehen;
        Distanz[1] = Total_Distanz_Laufen;
        Distanz[2] = Total_Distanz_Radfahren;

        Total_Zeit_Gehen = settings.getInt("Total_Zeit_Gehen" , Total_Zeit_Gehen);
        Total_Zeit_Laufen = settings1.getInt("Total_Zeit_Laufen" , Total_Zeit_Laufen);
        Total_Zeit_Radfahren = settings2.getInt("Total_Zeit_Radfahren" , Total_Zeit_Radfahren);
        Zeit[0] = Total_Zeit_Gehen;
        Zeit[1] = Total_Zeit_Laufen;
        Zeit[2] = Total_Zeit_Radfahren;

        now = Calendar.getInstance();

        radioGroup = findViewById(R.id.radioGroup);


        Button Calc = (Button) findViewById(R.id.Calc);
        Calc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                int Distanz_input = intent.getIntExtra(MainActivity.EXTRA_NUMBER, 0);

                if (Distanz_input != 0){

                    Datenaktualisierung.setText("");
                    int Distanz_Input = Distanz_input;
                    startChronometer(view);
                    double[] Weight = {Zeit[0]/Distanz[0] , Zeit[1]/Distanz[1] , Zeit[2]/Distanz[2]};



                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);

                    String Method = radioButton.getText().toString();
                    switch (Method) {
                        case "Gehen":
                            Result1 = Weight[0] * Distanz_Input;
                            Result = (int) Result1;
                            now.add(Calendar.SECOND, Result);
                            break;

                        case "Laufen":
                            Result1 = Weight[1] * Distanz_Input;
                            Result = (int) Result1;
                            now.add(Calendar.SECOND, Result);
                            break;
                        case "Radfahren":
                            Result1 = Weight[2] * Distanz_Input;
                            Result = (int) Result1;
                            now.add(Calendar.SECOND, Result);
                            break;
                    }
                    String Seconds = ":" ,minutes = ":",hours = "";
                    if (now.get(Calendar.MINUTE) < 10){minutes = ":0";}
                    if (now.get(Calendar.SECOND) < 10) {Seconds = ":0";}
                    if (now.get(Calendar.HOUR_OF_DAY) < 10) {hours = "0";}
                    TextView Time = (TextView) findViewById(R.id.editTextTime);
                    Time.setText(hours +  now.get(Calendar.HOUR_OF_DAY) +
                            minutes +
                            now.get(Calendar.MINUTE) +
                            Seconds +
                            now.get(Calendar.SECOND));
                    tStart = SystemClock.elapsedRealtime();


                }}
        });

        Button Cal = (Button) findViewById(R.id.Cal);
        Cal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                int Distanz_input = intent.getIntExtra(MainActivity.EXTRA_NUMBER, 0);

                if (Distanz_input > 0){
                    pauseChronometer(view);
                    resetChronometer(view);

                    int Distanz_Input = Distanz_input;


                    tEnd = SystemClock.elapsedRealtime();
                    long tDelta = tEnd - tStart;
                    double elapsedSeconds = tDelta / 1000.0;
                    int TRip_time = (int) elapsedSeconds;

                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);

                    String Method = radioButton.getText().toString();
                    switch (Method) {
                        case "Gehen":
                            Zeit[0] += TRip_time;
                            Distanz[0] += Distanz_Input;
                            Total_Distanz_Gehen = (int) Distanz[0];
                            Total_Zeit_Gehen = (int) Zeit [0];
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
                            SharedPreferences.Editor editor =settings.edit();
                            editor.putInt("Total_Distanz_Gehen" , Total_Distanz_Gehen);
                            editor.putInt("Total_Zeit_Gehen" , Total_Zeit_Gehen);
                            editor.commit();

                            break;

                        case "Laufen":
                            Zeit[1] += TRip_time;
                            Distanz[1] += Distanz_Input;
                            Total_Distanz_Laufen = (int) Distanz[1];
                            Total_Zeit_Laufen = (int) Zeit[1];
                            SharedPreferences settings1 = getSharedPreferences(PREFS_NAME,0);
                            SharedPreferences.Editor editor1 =settings1.edit();
                            editor1.putInt("Total_Distanz_Laufen" , Total_Distanz_Laufen);
                            editor1.putInt("Total_Zeit_Laufen" , Total_Zeit_Laufen);
                            editor1.commit();



                            break;
                        case "Radfahren":
                            Zeit[2] += TRip_time;
                            Distanz[2] += Distanz_Input;

                            Total_Distanz_Radfahren = (int) Distanz[2];
                            Total_Zeit_Radfahren = (int) Zeit [2];
                            SharedPreferences settings2 = getSharedPreferences(PREFS_NAME,0);
                            SharedPreferences.Editor editor2 =settings2.edit();
                            editor2.putInt("Total_Distanz_Radfahren" , Total_Distanz_Radfahren);
                            editor2.putInt("Total_Zeit_Radfahren" , Total_Zeit_Radfahren);
                            editor2.commit();


                            break;
                    }

                    TextView Time = (TextView) findViewById(R.id.editTextTime);
                    Time.setText("");

                    now.add(Calendar.SECOND, -1* Result);

                    Datenaktualisierung.setText("Ihre Daten wurden aktualisiert");



                }}
        });


    }
    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "Selected Radio Button: " + radioButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }
    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
}