package com.folio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.folio.common.Requests;
import com.folio.user.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ((TextView)findViewById(R.id.username)).setText(User.user.getUsername());
    }

    public void submitProjectPost(View view){
        String title = ((TextView)findViewById(R.id.post_title_input)).getText().toString();
        String description = ((TextView)findViewById(R.id.post_decription_input)).getText().toString();
        String skills = ((TextView)findViewById(R.id.project_skills_input)).getText().toString();
        String[] skillsArray = (String[])Arrays.asList(skills.split("\\s*,\\s*")).toArray();
        String skillsJson = "[]";
        try{
            skillsJson = new JSONArray(skillsArray).toString();
        }catch (JSONException e){
            e.printStackTrace();
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("function", "submit");
        data.put("username", User.user.getUsername());
        data.put("title", title);
        data.put("description", description);
        data.put("skills", skillsJson);
        Requests.postRequestToken(false, Urls.PROJECT_POST, data, new Requests.RequestOperation() {
            @Override
            public JSONObject onReply(String reply) {
                return null;
            }

            @Override
            public JSONObject onFail(String reply) {
                return null;
            }
        });

        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }

    public void submitUserPost(View view){
        String summary = ((TextView)findViewById(R.id.user_post_summary)).getText().toString();

        HashMap<String, String> data = new HashMap<>();
        data.put("function", "submit");
        data.put("username", User.user.getUsername());
        data.put("summary", summary);
        Requests.postRequestToken(false, Urls.USER_POST, data, new Requests.RequestOperation() {
            @Override
            public JSONObject onReply(String reply) {
                System.out.println("um...." + reply);
                return null;
            }

            @Override
            public JSONObject onFail(String reply) {
                return null;
            }
        });

        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }
}
