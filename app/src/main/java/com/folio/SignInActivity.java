package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.folio.common.Auth;
import com.folio.common.Requests;
import com.folio.user.User;

import org.json.JSONException;
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
                    public JSONObject onReply(String reply) {
                        // Update the current access token as the user has successfully signed in
                        User.LOGGED_IN = true;
                        try{
                            Auth.auth.setToken(new JSONObject(reply));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getBaseContext(), MainFeedActivity.class);
                        User.user = new User(((TextView)findViewById(R.id.signin_username_input)).getText().toString());
                        startActivity(intent);
                        return null;
                    }
                    @Override
                    public JSONObject onFail(String reply){
                        System.out.println(reply);
                        return null;
                    }
                }
        );
    }
}
