package com.folio.utils;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Web {
    public static class RequestTask extends AsyncTask<String, Void, String> {
        TextView resultText;
        ArrayList<String> results = new ArrayList<>();

        public void setResultText(TextView resultText){
            this.resultText = resultText;
        }

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;

            try {
                //Create connection
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                connection.setRequestProperty("Content-Length",
                        Integer.toString(urls[1].getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                //Send request
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(urls[1]);
                wr.close();

                //Get Response
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
                String line;
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();


                // Parse the JSON
                try {
                    JSONObject json = new JSONObject(response.toString());
                    return (String)json.get(urls[2]);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            this.resultText.setText(result);
        }
    }
}
