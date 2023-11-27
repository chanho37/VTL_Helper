package com.example.uam_user;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnector extends Thread{

    public void run(){
        try{

            URL url = new URL("http://127.0.0.1:5000/get_integer");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            if(conn != null){
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");

                int resCode = conn.getResponseCode();
                int HTTP_OK = HttpURLConnection.HTTP_OK;

                Log.d("JsonParsing", "rescode : " + resCode);
                Log.d("JsonParsing", "HTTP_OK : " + HTTP_OK);

            }
        }catch (Exception e){

        }
    }


}
