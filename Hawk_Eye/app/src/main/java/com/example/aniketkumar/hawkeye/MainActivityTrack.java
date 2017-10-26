package com.example.aniketkumar.hawkeye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import java.util.HashMap;

public class MainActivityTrack extends AppCompatActivity {

    private String TAG = MainActivityTrack.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;
  Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_track);
        context=getApplicationContext();
        contactList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivityTrack.this,"downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.43.187:1234/hawk/username.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("ID");
                        String name = c.getString("NAME");
                        Log.e("Id ",id);
                        Log.d("Name",name);



                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("ID", id);
                        contact.put("NAME", name);


                        // adding contact to contact list
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d("TAGG::",contactList.toString());
            ListAdapter adapter = new SimpleAdapter(MainActivityTrack.this, contactList,
                    R.layout.list_item, new String[]{ "ID","NAME"},
                    new int[]{R.id.ID, R.id.NAME});
//            lv.setAdapter(adapter);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView tv;

                    tv=(TextView) view.findViewById(R.id.ID);
                    String value=tv.getText().toString();

                   //  Log.e("TAG: :::",value);

                    BackgroundTask1 back=new BackgroundTask1();
                    back.execute(value);


                }
            }
              );
        }
    }


    public class BackgroundTask1 extends AsyncTask<String,Void,Void> {

        String res;
        String ID;
        HashMap<String, String> contact = new HashMap<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            String user_url="http://192.168.43.187:1234/hawk/retrieve.php";

            String id=params[0];
            ID=id;


          //  Log.d("TAGG::",id);
            try {
                URL url =new URL(user_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("ID","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8");
                Log.d("TAGG","data="+data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream is=new BufferedInputStream(httpURLConnection.getInputStream());
                res=convertStreamToString(is);
                Log.d("results","res="+res);

            } catch (MalformedURLException e) {
                Log.d("TAGG::","error1="+ e.toString());
            } catch (IOException e) {
                Log.d("TAGG::","error2="+ e.toString());
            }
//        if (res != null) {
//            try {
//                JSONObject jsonObj = new JSONObject(res);
//                // Getting JSON Array node
//                JSONArray contacts = jsonObj.getJSONArray("result");
//
//                // looping through All Contacts
//                for (int i = 0; i < contacts.length(); i++) {
//                    JSONObject c = contacts.getJSONObject(i);
//                    String latitude = c.getString("LATITUDE");
//                    String longitude = c.getString("LONGITUDE");
//                    Log.e("LATITUDE ", latitude);
//                    Log.d("LONGITUDE",longitude);
//
//
//
//
//
//                    // tmp hash map for single contact
//
//
//                    // adding each child node to HashMap key => value
//
//
//
//                    // adding contact to contact list
//
//                }
//                Log.e("TAG:::::",contact.toString());
//            } catch (final JSONException e) {
//                Log.e("TAG", "Json parsing error: " + e.getMessage());
//
//                    }
//
//
//            }
//
//         else {
//            Log.e("TAG", "Couldn't get json from server.");
//
//        }



            return null;
        }



        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("RESULT",res);
            Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("result",res);
            intent.putExtra("ID",ID);

            startActivity(intent);

        }

//    public void postData() {
//        // Create a new HttpClient and Post Header
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://www.yoursite.com/script.php");
//
//        try {
//            // Add your data
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("id", "12345"));
//            nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//        }
//    }

    }



}
//    EditText editText;
//    TextView textView;
//    String lat1;
//    String lon1;
//    int p = 0;
//    int q = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_track);
//        TextView ip = (TextView) findViewById(R.id.ip);
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.ankit);


//        BackgroundTask backgroundTask=new BackgroundTask();
//        backgroundTask.execute();

        //parse json data
//        BackgroundTask backgroundTask =new BackgroundTask ();
//        backgroundTask.execute();
//        for (int i = 1; i < 20; i++) {
//            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            LinearLayout l = new LinearLayout(this);
//            l.setOrientation(LinearLayout.HORIZONTAL);
//            Button b = new Button(this);
//            TextView textView = new TextView((this));
//            TextView text = new TextView(this);

            // textView.setTextSize(20);
//            textView.setText("IDkjagg845123098TYHUJIMKL,.");
//            textView.setWidth(30);
//            textView.setHeight(20);
//        //    textView.setPadding(500,0,0,0);
//            text.setText("");
//            text.setHeight(150);
//            text.setWidth(500);
//            text.setBackgroundColor(Color.YELLOW);
//            Button bU = new Button(this);
//            bU.setText("FUCK");
//            bU.setId(1 + i);
//            bU.setWidth(30);
//            bU.setHeight(20);
//            b.setText("TRACK");
//            b.setId(100 + i);
//            b.setWidth(30);
//            b.setHeight(20);
//            // b.layout(10,100*i,0,0);
//            l.addView(b, p);
//            l.addView(bU, p);
//            l.addView(text, p);
//            layout.addView(l);
//        }
//        String result= null;
//        while (result==null)
//              if(result==null)
//              {
//                  Log.d("Tag::","NULL HA ");
//                   result=backgroundTask.sendString();
//
//
//
//              }

//
//        String userurl = "http://169.254.45.146:1234/hawk/username.php";
//        String result;
//        InputStream is = null;
//        try {
//            URL url = new URL(userurl);
//            Log.d("Log", "CONNECT1");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//            httpURLConnection.setRequestMethod("GET");
//            Log.d("Log", "CONNECTED");
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setDoInput(true);
//            is = new BufferedInputStream(httpURLConnection.getInputStream());
//
//        } catch (MalformedURLException e) {
//            Log.d("TAG::","error");
//            e.printStackTrace();
//        } catch (IOException e) {
//            Log.d("Log::","error2");
//            e.printStackTrace();
//        }
//        //convert response to string
//        Log.d("TAG::","Aniket");
//        try {
//            Log.e("TAG::","45645");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            //  q = 1;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//                //  Toast.makeText(getApplicationContext(), “Input Reading pass”, Toast.LENGTH_SHORT).show();
//            }
//            is.close();
//            result = sb.toString();
//            Log.d("TAG::",result);
//
//        } catch (Exception e) {
//            Log.e("log_tag", "Error converting result123" + e.toString());
//            //  Toast.makeText(getApplicationContext(), "Input reading fail", Toast.LENGTH_SHORT).show();
//
//        }
//
//Log.d("TAG::","TrackACtivity123456");









//
//    WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//    String p = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
   //       ip.setText("Your IP: "+ip);










//        editText=(EditText)findViewById(R.id.editText);
//        Button button=(Button)findViewById(R.id.button);
//        Button locate=(Button)findViewById(R.id.locate);
//        textView =(TextView)findViewById(R.id.textView);
//        locate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                connect(view);
//            }
//        });
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String s1=textView.getText().toString();
//                String s2=textView.getText().toString();
//                if(s1.equals("")||s2.equals("First press Connect button"))
//                {
//                    textView.setText("First press Connect button");
//                }
//                else if(editText.getText().toString().equals("")||editText.getText().toString().equals("Server is connecting..."))
//                {
//                    editText.setText("Server is connecting...");
//                }
//                else {
//                    textView.setText("Connected");
//                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
//                    intent.putExtra("message", lat1);
//                    startActivity(intent);
//                }
//            }
//
//        });




//
//     public void connect(View view)
//     {
//         textView.setText("Connecting...");
//         Server server= new Server();
//         server.setEventListener(this);
//         server.startListeming();
//     }
//     public  void display(String message)
//    {
//       SharedPreferences sharedPreferences= getSharedPreferences("com.example.aniketkumar.hawkeye", Context.MODE_PRIVATE);
//       SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putString("message",message);
//        editor.commit();
//        lat1=message;
//        editText.setText(message);
//    }





