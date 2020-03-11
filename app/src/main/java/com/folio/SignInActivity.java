package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.folio.common.Auth;
import com.folio.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;

import okhttp3.Authenticator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signIn(null);
    }

    public void signIn(View view){
        OkHttpClient client = new OkHttpClient();
        final String username;
        String tmpusername = ((TextView)findViewById(R.id.signin_username_input)).getText().toString();
        username = tmpusername;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "password")
                .addFormDataPart("username", username)
                .addFormDataPart("password", ((TextView)findViewById(R.id.signin_password_input)).getText().toString())
                .build();
        Request request = new Request.Builder()
                .url(Urls.AUTH_GET_TOKEN)
                .addHeader("Authorization",Credentials.basic(Auth.ANDROID_APP_CLIENT, Auth.ANDROID_APP_SECRET))
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                try{
                    JSONObject json = new JSONObject(myResponse);
                    if(response.isSuccessful()){
                        validLogin(json.getString("access_token"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    // Perform login stuff here
    private void validLogin(String accessToken){
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("access_token", accessToken).build();
        Request request = new Request.Builder()
                .url(Urls.AUTH_VALIDATE_LOGIN)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                try{
                    JSONObject json = new JSONObject(myResponse);
                    if(response.isSuccessful() && json.getString("success")=="true"){
                        finishLogin();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        User.LOGGED_IN = true;
        User.user = new User("bob");
    }

    private void finishLogin(){
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }
}
