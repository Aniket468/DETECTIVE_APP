package com.example.aniketkumar.hawkeye;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
 * Created by Aniket Kumar on 08-Oct-17.
 */

public class BackgroundNotes extends AsyncTask<String,Void,Void> {
    Context ctx;
    BackgroundNotes(Context ctx)
    {
        this.ctx=ctx;
    }


    @Override
    protected void onPreExecute() {
        Toast.makeText(ctx,"Saving notes !",Toast.LENGTH_SHORT).show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... params) {
        String user_url="http://192.168.43.187:1234/hawk/notes.php";


            String notes = params[0];
            String id = params[1];

            Log.d("TAGG:::","notes="+notes+"id="+id);
            try {
                URL url =new URL(user_url);
                HttpURLConnection httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("NOTES","UTF-8")+"="+URLEncoder.encode(notes,"UTF-8")+"&"+
                        URLEncoder.encode("ID","UTF-8")+"="+ URLEncoder.encode(id,"UTF-8");
                Log.d("TAGG","data="+data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream is=new BufferedInputStream(httpURLConnection.getInputStream());

                String res=convertStreamToString(is);
                Log.d("TAGG:::","res="+res);

            } catch (MalformedURLException e) {
                Log.d("TAGG:::","error1="+ e.toString());
            } catch (IOException e) {
                Log.d("TAGG:::","error2="+ e.toString());
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
        Toast.makeText(ctx,"NOTES SAVED !",Toast.LENGTH_SHORT).show();
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
