package com.folio.utils;
import android.os.AsyncTask;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Web {
//    public static class RequestTask extends AsyncTask<String, Void, JSONObject> {
//        String[] args;
//        inflater
//
//        public void setText(T){
//            this.text = text;
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            HttpURLConnection connection = null;
//            this.args = args;
//            try {
//                URL url = new URL(args[0]);
//                URLConnection con = url.openConnection();
//                HttpURLConnection http = (HttpURLConnection)con;
//                http.setRequestMethod("POST");
//                http.setDoOutput(true);
//                Map<String,String> arguments = new HashMap<>();
//                arguments.put("id", args[1]);
//                StringJoiner sj = new StringJoiner("&");
//                for(Map.Entry<String,String> entry : arguments.entrySet())
//                    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
//                            + URLEncoder.encode(entry.getValue(), "UTF-8"));
//                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
//                int length = out.length;
//                http.setFixedLengthStreamingMode(length);
//                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//                http.connect();
//                try(OutputStream os = http.getOutputStream()) {
//                    os.write(out);
//                }
//
//                //Get Response
//                InputStream is = http.getInputStream();
//                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//                StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    response.append(line);
//                    response.append('\r');
//                }
//                rd.close();
//                // Parse the JSON
//                try {
//                    this.json = new JSONObject(response.toString());
//                    return json;
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                return null;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            } finally {
//                if (connection != null) {
//                    connection.disconnect();
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject json) {
//            if(json != null){
//                try {
//                    for (int i = 0; i < text.length; i++) {
//                        this.text[i].setText(json.getString(this.args[2 + i]));
//                    }
//                }catch(JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
