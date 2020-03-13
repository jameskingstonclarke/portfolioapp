package com.folio.common;
import androidx.arch.core.util.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Requests {


    public interface RequestOperation{
        JSONObject onReply(String reply);
        JSONObject onFail(String reply);
    }

    /**
     * Send an authenticated post request using a token
     * @param url
     * @param data
     * @param requestOperation
     */
    public static void postRequestToken(boolean authHeader, String url, HashMap<String, String> data, RequestOperation requestOperation){
        try {
            data.put("access_token", Auth.auth.getToken().getString("access_token"));
            postRequestRaw(authHeader, url, data, requestOperation);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * Send an un-authenticated post request
     * @param authHeader Whether the client authorization should be put in the header
     * @param url
     * @param data
     * @param requestOperation
     */
    public static void postRequestRaw(boolean authHeader, String url, HashMap<String, String> data, final RequestOperation requestOperation){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            builder.addEncoded(entry.getKey(), entry.getValue());
        }
        Request request;
        if(authHeader){
            request = new Request.Builder()
                    .url(url)
                    .addHeader("ContentType", "application/x-www-form-urlencoded")
                    .addHeader("Authorization", Credentials.basic(Auth.ANDROID_APP_CLIENT, Auth.ANDROID_APP_SECRET))
                    .post(builder.build())
                    .build();
        }else{
            request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .build();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                if(response.isSuccessful()){
                    requestOperation.onReply(myResponse);
                }else{
                    requestOperation.onFail(myResponse);
                }
            }
        });
    }
}
