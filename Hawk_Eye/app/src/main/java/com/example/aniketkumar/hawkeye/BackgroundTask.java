package com.example.aniketkumar.hawkeye;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.zip.DeflaterInputStream;

/**
 * Created by Aniket Kumar on 10-Oct-17.
 */

public class BackgroundTask extends AsyncTask<String,Void,Void> {
 Context context;
public BackgroundTask(Context context)
{
    this.context=context;
}
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


            Log.d("TAGG::",id);
            try {
                URL url =new URL(user_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data=URLEncoder.encode("ID","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8");
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
        Intent intent=new Intent(context,MapsActivity.class);
        intent.putExtra("result",res);
        intent.putExtra("ID",ID);
        context.startActivity(intent);

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
