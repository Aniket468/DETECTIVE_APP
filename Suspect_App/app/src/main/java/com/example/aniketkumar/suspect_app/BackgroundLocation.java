package com.example.aniketkumar.suspect_app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Aniket Kumar on 09-Oct-17.
 */

public class BackgroundLocation extends AsyncTask <String,Void,Void> {


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Void doInBackground(String... params) {
        String geo_url="http://192.168.43.187:1234/hawk/location.php";
        String id,lati,longi;
        id=params[0];
        lati=params[1];
        longi=params[2];
        try {
            URL url =new URL(geo_url);
            HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            String data= URLEncoder.encode("ID","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"+
                    URLEncoder.encode("LATITUDE","UTF-8")+"="+URLEncoder.encode(lati,"UTF-8")+"&"+
                    URLEncoder.encode("LONGITUDE","UTF-8")+"="+URLEncoder.encode(longi,"UTF-8");
            Log.d("TAGG","data="+data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream is=new BufferedInputStream(httpURLConnection.getInputStream());

            String res=convertStreamToString(is);
            Log.d("TAGG::","res="+res);

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
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
