package com.folio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.folio.common.Auth;
import com.folio.common.Requests;
import com.folio.common.UI;
import com.folio.user.User;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
    
    public void signIn(final View view){
        final Context context = this;
        HashMap<String, String> data = new HashMap<>();
        data.put("grant_type", "password");
        data.put("username", ((TextView)findViewById(R.id.signin_username_input)).getText().toString());
        data.put("password", ((TextView)findViewById(R.id.signin_password_input)).getText().toString());
        Requests.postRequestRaw(true, Urls.AUTH_GET_TOKEN, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(String reply) {
                        System.out.println(reply);
                        // Update the current access token as the user has successfully signed in
                        User.LOGGED_IN = true;
                        try{
                            Auth.auth.setToken(new JSONObject(reply));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        User.user = new User(((TextView)findViewById(R.id.signin_username_input)).getText().toString());
                        Intent intent = new Intent(getBaseContext(), NavigationActivity.class);
                        startActivity(intent);
                        return null;
                    }
                    @Override
                    public JSONObject onFail(String reply){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UI.popup(context, view, "Incorrect username or password!");
                            }
                        });
                        return null;
                    }
                }
        );
    }
}
