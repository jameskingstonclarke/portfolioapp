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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

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
    }

    public void submitProjectPost(View view){
        System.out.println("Submitting post...");
        String title = ((TextView)findViewById(R.id.post_title_input)).getText().toString();
        String description = ((TextView)findViewById(R.id.post_decription_input)).getText().toString();
        String skills = ((TextView)findViewById(R.id.project_skills_input)).getText().toString();
        String[] skillsArray = (String[])Arrays.asList(skills.split("\\s*,\\s*")).toArray();
        System.out.println(title);
        System.out.println(description);

        OkHttpClient client = new OkHttpClient();

        String postSubmitURL = Urls.PROJECT_POST;
        String skillsJson = "[]";
        try{
            skillsJson = new JSONArray(skillsArray).toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "0")
                .addFormDataPart("title", title)
                .addFormDataPart("description", description)
                .addFormDataPart("skills", skillsJson)
                .build();

        Request request = new Request.Builder()
                .url(postSubmitURL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("successful!");
                }else{
                    System.out.println("unsuccessful!");
                }
                System.out.println(response.body().string());
            }
        });

        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }

    public void submitUserPost(View view){
        System.out.println("Submitting post...");
        String summary = ((TextView)findViewById(R.id.user_post_summary)).getText().toString();

        OkHttpClient client = new OkHttpClient();

        String postSubmitURL = "http://jameskingstonclarke.com/appapi/user_post_submit.php";

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("user_id", "0")
                .addFormDataPart("summary", summary)
                .build();

        Request request = new Request.Builder()
                .url(postSubmitURL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    System.out.println("successful!");
                }else{
                    System.out.println("unsuccessful!");
                }
            }
        });

        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }
}
