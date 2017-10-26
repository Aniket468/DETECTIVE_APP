package com.example.aniketkumar.hawkeye;

import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Aniket Kumar on 25-Sep-17.
 */

public class ReceiveThread extends AppCompatActivity implements Runnable{
    Socket socket;

    public void run(){
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(socket.getInputStream());
            // Reading input and printing if any input received
            while(true){
                String msg = dis.readUTF();
                EditText editText=(EditText)findViewById(R.id.editText);
                editText.setText(msg);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public ReceiveThread(Socket sock){
        this.socket = sock;
    }
}