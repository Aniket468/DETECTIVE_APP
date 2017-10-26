package com.example.aniketkumar.hawkeye;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.HashMap;

public class NotesActivity extends AppCompatActivity {
    EditText editText;
    String id;
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        lv = (ListView) findViewById(R.id.list);
        contactList = new ArrayList<>();
        editText = (EditText) findViewById(R.id.edittext);
        Bundle extras = getIntent().getExtras();
        id = (String) extras.get("ID");

    }

    public void click(View view) {
        String value = editText.getText().toString();
        if (value.equals("")) {
            Log.e("Tag::", "not set" + value);
            Toast.makeText(getApplicationContext(), "Empty notes can't be save :)", Toast.LENGTH_SHORT).show();
        } else {

            BackgroundNotes backgroundNotes = new BackgroundNotes(this);
            backgroundNotes.execute(value, id);
            editText.setText("");

        }
    }

    public void notesclick(View view) {
        contactList.clear();

        BackgroundTask1 backgroundTask1 = new BackgroundTask1();
        backgroundTask1.execute(id);
    }


    public class BackgroundTask1 extends AsyncTask<String, Void, Void> {

        String res;
        String ID;
        HashMap<String, String> contact = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            String user_url = "http://192.168.43.187:1234/hawk/retrievenotes.php";

            String id = params[0];
            ID = id;


          //  Log.d("TAGG::", id);
            try {
                URL url = new URL(user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
             //   Log.d("TAGG", "data=" + data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
                res = convertStreamToString(is);
              //  Log.d("results", "res=" + res);

            } catch (MalformedURLException e) {
                Log.d("TAGG::", "error1=" + e.toString());
            } catch (IOException e) {
                Log.d("TAGG::", "error2=" + e.toString());
            }
            if (res != null) {
                try {
                    JSONObject jsonObj = new JSONObject(res);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String notes = c.getString("NOTES");
                        String time = c.getString("TIME");
                     //   Log.e("notes ", notes);
                      //  Log.d("Time", time);


                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("NOTES", notes);
                        contact.put("TIME", time);


                        // adding contact to contact list
                        contactList.add(contact);

                        // tmp hash map for single contact


                        // adding each child node to HashMap key => value


                        // adding contact to contact list

                    }
               //     Log.e("TAG:::::", contact.toString());
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }


            } else {
                Log.e("TAG", "Couldn't get json from server.");
                Toast.makeText(getApplicationContext(),
                        "Couldn't get data from server !",
                        Toast.LENGTH_LONG).show();
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
        //    Log.e("RESULT", res);
            final ListAdapter adapter = new SimpleAdapter(NotesActivity.this, contactList,
                    R.layout.list_notes, new String[]{"NOTES", "TIME"},
                    new int[]{R.id.NOTES, R.id.TIME});
//            lv.setAdapter(adapter);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                          @Override
                                          public void onItemClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                                              AlertDialog.Builder builder = new AlertDialog.Builder(NotesActivity.this);
                                              builder.setTitle("Deletion Warning ");
                                              builder.setMessage("Are you really want to delete this note.");
                                              builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                  @Override
                                                  public void onClick(DialogInterface dialog, int which) {
                                                      dialog.cancel();
                                                      contactList.clear();
                                                      TextView textView=(TextView) view.findViewById(R.id.NOTES);

                                                      String value= textView.getText().toString();
                                                      BackgroundDelete backgroundDelete =new BackgroundDelete();
                                                      backgroundDelete.execute(id,value);


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
                                      }
            );

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

    public class BackgroundDelete extends AsyncTask<String, Void, Void> {

        String res;
        String ID;
        HashMap<String, String> contact = new HashMap<>();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            Log.e("TAG::","Clear");

        }

        @Override
        protected Void doInBackground(String... params) {
            String user_url = "http://192.168.43.187:1234/hawk/deletenotes.php";

            String id = params[0];
            ID = id;
            String notes = params[1];


            Log.d("TAGG::", id + "  " + notes);
            try {
                URL url = new URL(user_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("NOTES", "UTF-8") + "=" + URLEncoder.encode(notes, "UTF-8");
                Log.d("TAGG", "data=" + data);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
                res = convertStreamToString(is);
                Log.d("results", "res=" + res);

            } catch (MalformedURLException e) {
                Log.d("TAGG::", "error1=" + e.toString());
            } catch (IOException e) {
                Log.d("TAGG::", "error2=" + e.toString());
            }

            if (res != null) {
                try {
                    JSONObject jsonObj = new JSONObject(res);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("result");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String notes1 = c.getString("NOTES");
                        String time = c.getString("TIME");
                        Log.e("notes ", notes1);
                        Log.d("Time", time);


                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("NOTES", notes1);
                        contact.put("TIME", time);


                        // adding contact to contact list
                        contactList.add(contact);

                        // tmp hash map for single contact


                        // adding each child node to HashMap key => value


                        // adding contact to contact list

                    }
                    Log.e("TAG:::::", contact.toString());
                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                }


            } else {
                Log.e("TAG", "Couldn't get json from server.");
                Toast.makeText(getApplicationContext(),
                        "Couldn't get data from server !",
                        Toast.LENGTH_LONG).show();
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

            Log.e("RESULT", res);
            ListAdapter adapter = new SimpleAdapter(NotesActivity.this, contactList,
                    R.layout.list_notes, new String[]{"NOTES", "TIME"},
                    new int[]{R.id.NOTES, R.id.TIME});
//
            lv.setAdapter(adapter);


        }

    }
}
