package com.example.aniketkumar.hawkeye;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.SslCertificate;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String la1,lo1;
    String Mmessage="12.2334,78.447";
    TextView editText;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;

    GoogleApiClient mGoogleApiClient;

    Marker mCurrLocationMarker;
    boolean firstRun = true;

    int p=0;
    int q=0;
    int check=1;
    private Marker mymarker;
    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        String message= (String) extras.get("result");
        ID=(String)extras.get("ID");
        Button button=(Button)findViewById(R.id.nearby);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("click","hua h");
                check=0;
                StringBuilder sbValue = new StringBuilder(sbMethod());
                Log.e("URL",""+sbValue);
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sbValue.toString());
            }
        });
        //Log.d(" IN MAP ",message);
        Mmessage=message;
        editText =(TextView) findViewById(R.id.editText);
        Button call =(Button)findViewById(R.id.call);
        Button past =(Button)findViewById(R.id.past);
        Button current=(Button)findViewById(R.id.current);
        Button notes=(Button)findViewById(R.id.notes);
        final EditText edittext=(EditText)findViewById(R.id.edittext);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent =new Intent(getApplicationContext(),NotesActivity.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
                startActivity(intent);

            }
        });
        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=1;
                editText.setText("Wait Updating your Location ");
            }
        });
        past.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                q=1;
                check=0;
                BackgroundTask2 backgroundTask2=new BackgroundTask2();
                backgroundTask2.execute(ID);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Call_Activity.class);
                startActivity(intent);
            }
        });




      /*  updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("Connecting...")||editText.getText().toString().equals(" Wait server is connecting...."))
                {
                    editText.setText(" Wait server is connecting....");
                }
                else {
                    connect(view);
                }
            }
        });*/

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

               // SharedPreferences sharedPreferences = getSharedPreferences("com.example.aniketkumar.hawkeye", Context.MODE_PRIVATE);
                 //Mmessage = sharedPreferences.getString("message", "");
//                 char[] ch = Mmessage.toCharArray();
//                 char[] lati = new char[30];
//                 char[] longi = new char[30];
//                 int p = 0;
//                 int k = 0;
//                 int l = 0, i;
//
//                 for (i = 0; i < ch.length; i++) {
//                     if (p == 0 && ch[i] != ',') {
//                         lati[k++] = ch[i];
//                     } else {
//                         if (p == 0) {
//                             i++;
//                             p = 1;
//                         }
//                         longi[l++] = ch[i];
//                     }
//                 }

        if (Mmessage != null) {
            try {
                JSONObject jsonObj = new JSONObject(Mmessage);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("result");

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String lati = c.getString("LATITUDE");
                    String longi = c.getString("LONGITUDE");
                    String time=c.getString("TIME");
                    la1=lati;
                    lo1=longi;
                    double lat = Double.parseDouble(lati);
                    double lon = Double.parseDouble(longi);
                    LatLng India = new LatLng(lat,lon);
                   // Log.e("lati ",""+lat);
                    //Log.d("longi",""+lon);
                    //Log.e("Time",time);

                      // mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Suspect").snippet(time));


                     if(i==contacts.length()-1)
                         mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Suspect Last Location").snippet(time)
                                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(India,17.0f));

                }
            } catch (final JSONException e) {
                Log.e("TAG", "Json parsing error: " + e.getMessage());
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
            Log.e("TAG", "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG).show();
                }
            });
        }




        }




//for automatic update after 15second

Handler h = new Handler();
    int delay = 15000; //15 seconds
    Runnable runnable;
    @Override
    protected void onStart() {
//start handler as activity become visible

        h.postDelayed(new Runnable() {
            public void run() {


//
                    if(!editText.getText().toString().equals("Connecting")&&check==1) {
                        editText.setText("Connecting");
                        BackgroundTask2 backgroundTask2=new BackgroundTask2();
                        backgroundTask2.execute(ID);
                        if(p==1) {
                            editText.setText("Location updated");
                            p=0;
                        }
                    }








                runnable=this;

                h.postDelayed(runnable, delay);
            }
        }, delay);

        super.onStart();
    }

    @Override
    protected void onPause() {
        h.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    public class BackgroundTask2 extends AsyncTask<String,Void,Void> {

        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            String user_url="http://192.168.43.187:1234/hawk/retrieve.php";

            String id=params[0];




         //   Log.d("TAGG::",id);
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
   editText.setText("Location Updated");
            if (res != null) {
                try {
                    mMap.clear();
                    JSONObject jsonObj = new JSONObject(res);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String latitude = c.getString("LATITUDE");
                        String longitude = c.getString("LONGITUDE");
                        String time=c.getString("TIME");
                        la1=latitude;
                        lo1=longitude;
                       // Log.e("LATITUDE ", latitude);
                        //Log.d("LONGITUDE",longitude);
                        double lat = Double.parseDouble(latitude);
                        double lon = Double.parseDouble(longitude);
                        LatLng Indi = new LatLng(lat,lon);
                        if(q==1)
                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Suspect").snippet(time));


                        if(i==contacts.length()-1) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Suspect Last Location").snippet(time)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Indi, 17.0f));
                            p = 1;
                        }
                    }


                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());

                }


            }

            else {
                Log.e("TAG", "Couldn't get json from server.");

            }

q=0;

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

//    public void connect()
//    {
//
//        editText.setText("Connecting...");
//        Server server= new Server();
//        server.setEventListener(this);
//        server.startListeming();
//    }
//    public  void display(String message)
//    {
//       // SharedPreferences sharedPreferences= getSharedPreferences("com.example.aniketkumar.hawkeye", Context.MODE_PRIVATE);
//        //SharedPreferences.Editor editor=sharedPreferences.edit();
//        //editor.putString("message",message);
//        //editor.commit();
//        //lat1=message;
//       editText.setText(message);
//        Mmessage=null;
//        Mmessage = message;
//        char[] ch = Mmessage.toCharArray();
//        char[] lati = new char[100];
//        char[] longi = new char[100];
//        int p = 0;
//        int k = 0;
//        int l = 0, i;
//
//        for (i = 0; i < ch.length; i++) {
//            if (p == 0 && ch[i] != ',') {
//                lati[k++] = ch[i];
//            } else {
//                if (p == 0) {
//                    i++;
//                    p = 1;
//                }
//                longi[l++] = ch[i];
//            }
//        }
//        String latitude = new String(lati);
//        String longitude = new String(longi);
//        double lat = Double.parseDouble(latitude);
//        double lon = Double.parseDouble(longitude);
//        LatLng India = new LatLng(lat,lon);
//        time.setToNow();
//        hou=time.hour;
//        min=time.minute;
//        sec=time.second;
//        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title("Suspect").snippet(""+hou+":"+min+":"+sec)
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(India,17.0f));
//    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.equals(mymarker))
        {
        }
        return false;
    }



    private class PlacesTask extends AsyncTask<String, Integer, String>
    {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            Log.d("result", "<><> result: " + result);
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParserTask
            parserTask.execute(result);
        }
    }

    private String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            Place_JSON placeJson = new Place_JSON();

            try {
                jObject = new JSONObject(jsonData[0]);

                places = placeJson.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            Log.d("Map", "list size: " + list.size());
            // Clears all the existing markers;
            if (!firstRun) {

                mMap.clear();
            }
            firstRun = false;

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);


                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("place_name");

                Log.d("Map", "place: " + name);
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(name).snippet("Suspects nearby Places")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
if(i==0)
{
    Double l1=Double.parseDouble(la1);
    Double l2=Double.parseDouble(lo1);

    mMap.addMarker(new MarkerOptions().position(new LatLng(l1, l2)).title("Suspect Last Location")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(l1, l2), 12.0f));
//    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(India,17.0f));
}

                // Getting vicinity


            }
        }
    }
    public class Place_JSON {

        /**
         * Receives a JSONObject and returns a list
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                /** Retrieves all the elements in the 'places' array */
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Invoking getPlaces with the array of json object
             * where each json object represent a place
             */
            return getPlaces(jPlaces);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         */
        private HashMap<String, String> getPlace(JSONObject jPlace)
        {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";

            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");

                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }









    public StringBuilder sbMethod() {
        //current location
      String mLatitude=la1;
        String mLongitude=lo1;

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + mLatitude + "," + mLongitude);
        sb.append("&radius=2500");
        sb.append("&types=" + "train_station|airport|bus_station");
        sb.append("&sensor=true");

        sb.append("&key=AIzaSyBvN7-4PKDpvpcw-dkTd_6-B5WuptirDSw");

        Log.d("Map", "<><>api: " + sb.toString());

        return sb;
    }
}

