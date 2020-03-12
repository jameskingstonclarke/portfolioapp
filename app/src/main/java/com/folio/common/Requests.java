package com.folio.common;
import androidx.arch.core.util.Function;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requests {

    public static void postRequestToken(String url, HashMap<String, String> data, final Function<JSONObject, Object> function){
        try {
            data.put("access_token", Auth.auth.getToken().getString("access_token"));
            postRequestRaw(url, data, function);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public static void postRequestRaw(String url, HashMap<String, String> data, final Function<JSONObject, Object> function){
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)iterator.next();
            builder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    try{
                        function.apply(new JSONObject(myResponse));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
