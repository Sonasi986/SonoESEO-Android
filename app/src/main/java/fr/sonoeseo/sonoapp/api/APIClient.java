package fr.sonoeseo.sonoapp.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import fr.sonoeseo.sonoapp.content.Content;

/**
 * Created by sonasi on 31/10/2017.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see http://www.gnu.org/licenses/
 */

public class APIClient {

    // Singleton of the class.
    private static APIClient instance;

    private APIClient() {
    }

    // Getter for the instance of the singleton.
    public static APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    /**
     * This function will verify if the user is connected to internet.
     *
     * @param ctx : The current context.
     * @return Boolean which describes whether the user is well connected.
     */
    public boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /*
    * This function make a POST request to the API.
    *
    * @param ctx: The current context.
    * @param link : The link of where the request will be make.
    * @param args : The post arguments seperated by "&".
     */
    public String request(Context ctx, String url, String params) {
        String result = "";
        URL urlO;
        if (APIClient.getInstance().isOnline(ctx)) {
            try {
                urlO = new URL(url);
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlO.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setReadTimeout(10000);
                    httpURLConnection.setConnectTimeout(15000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);

                    if (params != null && !params.isEmpty()) {
                        OutputStream os = httpURLConnection.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(params);

                        writer.flush();
                        writer.close();
                        os.close();
                    }
                    int responseCode = httpURLConnection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result += line;
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * This AsyncTask will send the push token of the App and user
     * to the API.
     */
    public static class RegisterNotification extends android.os.AsyncTask<String, Void, String> {
        private Context ctx;
        private String push;
        public RegisterNotification(Context ctx, String push) {
            this.ctx = ctx;
            this.push = push;
        }

        @Override
        protected String doInBackground(String... urls) {
            String args = "login=" + Content.user.getLogin() + "&token=" + Content.user.getToken()
                    + "&push=" + push;
            if (APIClient.getInstance().isOnline(ctx)) {
                return APIClient.getInstance().request(ctx, APIConstants.API_NOTIFICATION, args);
            } else {
                return "";
            }
        }
    }

}
