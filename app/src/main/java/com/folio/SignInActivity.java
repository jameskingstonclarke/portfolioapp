package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.folio.common.Auth;
import com.folio.common.Requests;
import com.folio.user.User;
import org.json.JSONObject;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        signIn(null);
    }
    
    public void signIn(View view){
        HashMap<String, String> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("username", ((TextView)findViewById(R.id.signin_username_input)).getText().toString());
        data.put("password", ((TextView)findViewById(R.id.signin_password_input)).getText().toString());
        Requests.postRequestRaw(Urls.AUTH_GET_TOKEN, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(JSONObject reply) {
                        // Update the current access token as the user has successfully signed in
                        User.LOGGED_IN = true;
                        Auth.auth.setToken(reply);
                        Intent intent = new Intent(getBaseContext(), MainFeedActivity.class);
                        User.user = new User(((TextView)findViewById(R.id.signin_username_input)).getText().toString());
                        startActivity(intent);
                        return null;
                    }
                    @Override
                    public JSONObject onFail(JSONObject reply){
                        System.out.println(reply);
                        return null;
                    }
                }
        );
    }

//    public void signIn(View view){
//        OkHttpClient client = new OkHttpClient();
//        final String username;
//        String tmpusername = ((TextView)findViewById(R.id.signin_username_input)).getText().toString();
//        username = tmpusername;
//        String password = ((TextView)findViewById(R.id.signin_password_input)).getText().toString();
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("grant_type", "password")
//                .addFormDataPart("username", username)
//                .addFormDataPart("password", password)
//                .build();
//        Request request = new Request.Builder()
//                .url(Urls.AUTH_GET_TOKEN)
//                .addHeader("Authorization",Credentials.basic(Auth.ANDROID_APP_CLIENT, Auth.ANDROID_APP_SECRET))
//                .post(requestBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String myResponse = response.body().string();
//                try{
//                    JSONObject json = new JSONObject(myResponse);
//                    if(response.isSuccessful()){
//                        validLogin(username, json);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    // Perform login stuff here
//    private void validLogin(final String username, final JSONObject token){
//        OkHttpClient client = new OkHttpClient();
//        RequestBody formBody = null;
//        try{
//            formBody = new FormBody.Builder().add("access_token", token.getString("access_token")).build();
//        }catch(JSONException e){
//            e.printStackTrace();
//        }
//        Request request = new Request.Builder()
//                .url(Urls.AUTH_VALIDATE_LOGIN)
//                .post(formBody)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String myResponse = response.body().string();
//                try{
//                    JSONObject json = new JSONObject(myResponse);
//                    if(response.isSuccessful() && json.getString("success")=="true"){
//                        finishLogin(username, json);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void finishLogin(String username, JSONObject token){
//        Auth.auth.setToken(token);
//        Intent intent = new Intent(this, MainFeedActivity.class);
//
//        User.LOGGED_IN = true;
//        User.user = new User(username);
//        startActivity(intent);
//    }
}
