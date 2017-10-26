package com.example.aniketkumar.suspect_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameID extends AppCompatActivity {

    EditText name,id;
   // Button save;
    String nam,ID;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_id);
        name=(EditText)findViewById(R.id.name);
        id=(EditText)findViewById(R.id.id);


    }
    public void save (View view)
    {
        nam=name.getText().toString();
        ID=id.getText().toString();
        Log.d("TAGG::","name="+name+"id="+id);
        SharedPreferences sharedPreferences= getSharedPreferences("com.example.aniketkumar.suspect_app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("NAME",nam);
        editor.putString("ID",ID);
        editor.commit();
        String check="user";
        BackgroundTask backgroundTask=new BackgroundTask(this);
        backgroundTask.execute(check,nam,ID);
        finish();
    }
}
