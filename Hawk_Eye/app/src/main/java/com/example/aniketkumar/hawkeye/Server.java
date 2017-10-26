package com.example.aniketkumar.hawkeye;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aniketkumar.hawkeye.ReceiveThread;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Aniket Kumar on 24-Sep-17.
 */

public class Server {
    Thread m_objThread;
    ServerSocket m_server;
    String m_strMessage;
    DataDisplay m_dataDisplay;
    Object m_connected;
   public Server()
   {

   }
   public void setEventListener(DataDisplay dataDisplay)
   {
       m_dataDisplay=dataDisplay;
   }
   public void startListeming()
   {
        m_objThread=new Thread(new Runnable() {
            @Override
            public void run() {
           try{
               m_server=new ServerSocket(2000);
               Socket connectedSocket =m_server.accept();
               Message clientmessage =Message.obtain();
               ObjectInputStream ois=new ObjectInputStream(connectedSocket.getInputStream());
               String strMessage=(String) ois.readObject();
               clientmessage.obj =strMessage;
               mHandler.sendMessage(clientmessage);
               ObjectOutputStream oos =new ObjectOutputStream(connectedSocket.getOutputStream());
               oos.writeObject("Connected");
              ois.close();
               oos.close();
               m_server.close();
           }
          catch (IOException e) {
              Message msg=Message.obtain();
              msg.obj=e.getMessage();
              mHandler.sendMessage(msg);
               e.printStackTrace();

           } catch (ClassNotFoundException e) {
               e.printStackTrace();
           }
            }
        });
       m_objThread.start();
   }
   Handler mHandler =new Handler()
   {
       public void handleMessage(Message status)
       {
           m_dataDisplay.display((status.obj.toString()));

       }
   };



}
