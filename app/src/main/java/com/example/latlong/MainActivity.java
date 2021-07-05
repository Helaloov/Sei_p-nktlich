package com.example.latlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText etPlace;
    EditText etPlace2;
    Button btSubmit;
    Button btSubmit2;
    TextView tvAddress;
    public static final String EXTRA_NUMBER = "com.example.application.latlong.EXTRA_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPlace = findViewById(R.id.et_place);
        etPlace2 = findViewById(R.id.et_place2);
        btSubmit = findViewById(R.id.bt_submit);
        btSubmit2 = findViewById(R.id.bt_submit2);
        tvAddress = findViewById(R.id.tv_address);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = etPlace.getText().toString();
                String address2 = etPlace2.getText().toString();
                Geolocation geolocation = new Geolocation();
                geolocation.getAddress(address,address2,getApplicationContext(),new GeoHandler());
                

            }
        });

        btSubmit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            double address;
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getDouble("address");
                    break;
                default:
                    address = 0;
            }
            int finallll = (int) address;
            String finalresult = String.valueOf(finallll);
            tvAddress.setText(finalresult );
        }
    }
    public void openActivity2() {
        int number = Integer.parseInt(tvAddress.getText().toString());
        Intent intent = new Intent(this, Activity2.class);
        intent.putExtra(EXTRA_NUMBER, number);
        startActivity(intent);
    }
}