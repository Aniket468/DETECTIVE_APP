package com.example.aniketkumar.hawkeye;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView ip=(TextView)findViewById(R.id.ip);
         WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String p = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        ip.setText("Your IP: "+p);

        Button developer=(Button)findViewById(R.id.developer);
        developer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(getApplicationContext(),MainActivityDeveloper.class);
                startActivity(i1);
            }
        });
        Button track=(Button)findViewById(R.id.track);
        track.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i3=new Intent(getApplicationContext(),MainActivityTrack.class);
                startActivity(i3);

            }
        });
    }
}
