package com.example.aniketkumar.suspect_app;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static int ACCESS_FINE_LOCATION_CONSTANT;
    Thread m_objThreadclient;
    EditText ediTtext ,ip;
    Socket clientSocket;
    Button connect;
    String ipserver;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences= getSharedPreferences("com.example.aniketkumar.suspect_app", Context.MODE_PRIVATE);
        message=sharedPreferences.getString("NAME","Default");
        Log.d("TAGG::","message="+message);
        if(message.equals("Default"))
        {
            Intent intent=new Intent(getApplicationContext(),NameID.class);
            startActivity(intent);
        }
        ediTtext = (EditText) findViewById(R.id.editText);
        connect=(Button)findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ipserver=ip.getText().toString();
                Intent i= new Intent(getApplicationContext(), Myservice.class);
               // potentially add data to the intent
                //i.putExtra("KEY1",ipserver);
                startService(i);

            }
        });
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

          // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                String s1 = location.toString();
                Double lati=location.getLatitude();
                Double longi=location.getLongitude();
                SharedPreferences sharedPreferences= getSharedPreferences("com.example.aniketkumar.suspect_app", Context.MODE_PRIVATE);
                message=sharedPreferences.getString("ID","Default");
                Log.d("TAGG::","message="+message);
                ediTtext.setText(s1);
                BackgroundLocation backgroundLocation=new BackgroundLocation();
                backgroundLocation.execute(message,""+lati,""+longi);
//                sendLocation(""+lati,""+longi);
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }


            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Need Location permission");
            builder.setMessage("This app needs location permission.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CONSTANT);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
       /* else if (permissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION,false)) {
            //Previously Permission Request was cancelled with 'Dont Ask Again',
            // Redirect to Settings after showing Information about why you need the permission
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Need location Permission");
            builder.setMessage("This app needs location permission.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant location", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }*/
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 2, locationListener);
        }

    }
    public void hide(View view)
    {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, com.example.aniketkumar.suspect_app.MainActivity.class); // activity which is first time open in manifiest file which is declare as <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }
   /* public void sendoutBroadcast(Location location)
    {
        Intent intent =new Intent();
        intent.setAction("com.example.aniketkumar.suspect_app");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);


    }*/

    public void sendLocation(final String lati,final  String longi) {
        m_objThreadclient = new Thread((new Runnable() {
            @Override
            public void run() {
                try {
                    String localhost= ipserver;
                    clientSocket = new Socket(localhost,2000);
                    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                    oos.writeObject(lati+","+longi);
                    Message editText = Message.obtain();

                    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                    String strMessage = (String) ois.readObject();
                    editText.obj = strMessage;
                    mHandler.sendMessage(editText);
                    oos.close();
                    ois.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));
        m_objThreadclient.start();

    }
    Handler mHandler =new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
           messageDisplay(msg.obj.toString());
        }
    };
    public void messageDisplay(String serverMessage)
    {
        ediTtext.setText(serverMessage);
    }
}