package com.example.zpfr3739.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ZPFR3739 on 22/08/2017.
 */
/*
public class WebServerConnection extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String args) {
Code à exécuter
//établir la connexion et échanger les
        URL url = null;
        try {
            url = new URL(args[0]);
        } catch(MalformedURLException e) {
            Log.e("ConnexionHTTP", "URL incorrect : " + e);
            return false;
        }
        URLConnection connexion = null;
        try {
            connexion = url.openConnection();
            connexion.setDoOutput(true);
        } catch(IOException e) {
            Log.e("ConnexionHTTP", "Connexion impossible : " + e);
            return false;
        }


    }

}
*/