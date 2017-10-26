package com.example.aniketkumar.hawkeye;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Call_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_);
        Button police=(Button)findViewById(R.id.police);
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent policeintent=new Intent(Intent.ACTION_DIAL);
                policeintent.setData(Uri.parse("tel:"+"100"));
                startActivity(policeintent);
            }
        });
         Button railways =(Button)findViewById(R.id.railways);
        railways.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent railwaysintent=new Intent(Intent.ACTION_DIAL);
                railwaysintent.setData(Uri.parse("tel:"+"1512"));
                startActivity(railwaysintent);
            }
        });
        Button ambulance=(Button)findViewById(R.id.ambulance);
        ambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ambulanceintent=new Intent(Intent.ACTION_DIAL);
                ambulanceintent.setData(Uri.parse("tel:"+"102"));
                startActivity(ambulanceintent);
            }
        });
    }
}
