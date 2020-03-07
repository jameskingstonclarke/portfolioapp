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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoveryFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_feed);

        OkHttpClient client = new OkHttpClient();

        String postGetURL = Urls.PROJECT_POST+"?function=all";
        Request postGetRequest = new Request.Builder().url(postGetURL).build();
        client.newCall(postGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    final JSONArray jsonArray;
                    JSONArray tempArray = null;
                    // Attempt to retrieve the post count
                    final int postCount;
                    int tempCount = 0;
                    try {
                        tempArray = new JSONArray(myResponse);
                        tempCount = tempArray.length();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    postCount = tempCount;
                    jsonArray = tempArray;
                    // Update the endless scroll UI
                    DiscoveryFeedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Dynamically invoke stuff here
                            ConstraintSet set = new ConstraintSet();
                            ConstraintLayout layout = findViewById(R.id.discoverProjectPostScrollConstraint);
                            LayoutInflater inflater = getLayoutInflater();
                            RelativeLayout relLayout, previousLayout=null;

                            for(int i = 0; i<postCount; i++) {
                                relLayout = (RelativeLayout) inflater.inflate(R.layout.project_post_layout, null);
                                relLayout.setId(View.generateViewId());
                                // Get the text views and update them accordingly
                                TextView postTitle = relLayout.findViewById(R.id.project_post_title);
                                TextView description = relLayout.findViewById(R.id.project_post_description);
                                TextView user = relLayout.findViewById(R.id.project_post_user);
                                TextView time = relLayout.findViewById(R.id.project_post_time);
                                try {
                                    JSONObject json = (JSONObject)jsonArray.get(i);
                                    postTitle.setText(json.getString("title"));
                                    description.setText(json.getString("description"));
                                    user.setText(json.getString("user_name"));
                                    time.setText(json.getString("time"));
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                                layout.addView(relLayout, i);
                                set.clone(layout);
                                if (previousLayout != null)
                                    set.connect(relLayout.getId(), ConstraintSet.TOP, previousLayout.getId(), ConstraintSet.BOTTOM, 60);
                                set.applyTo(layout);
                                previousLayout = relLayout;
                            }
                        }
                    });
                }
            }
        });

        String userGetURL = Urls.USER_POST+"?function=all";
        postGetRequest = new Request.Builder().url(userGetURL).build();
        client.newCall(postGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String myResponse = response.body().string();
                    final JSONArray jsonArray;
                    JSONArray tempArray = null;
                    // Attempt to retrieve the post count
                    final int postCount;
                    int tempCount = 0;
                    try {
                        tempArray = new JSONArray(myResponse);
                        tempCount = tempArray.length();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    postCount = tempCount;
                    jsonArray = tempArray;
                    // Update the endless scroll UI
                    DiscoveryFeedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Dynamically invoke stuff here
                            ConstraintSet set = new ConstraintSet();
                            ConstraintLayout layout = findViewById(R.id.discoverUserPostScrollConstraint);
                            LayoutInflater inflater = getLayoutInflater();
                            RelativeLayout relLayout, previousLayout=null;

                            for(int i = 0; i<postCount; i++) {
                                relLayout = (RelativeLayout) inflater.inflate(R.layout.user_post_layout, null);
                                relLayout.setId(View.generateViewId());
                                // Get the text views and update them accordingly
                                TextView postName = relLayout.findViewById(R.id.user_post_name);
                                TextView postSummary = relLayout.findViewById(R.id.user_post_summary);
                                try {
                                    JSONObject json = (JSONObject)jsonArray.get(i);
                                    postName.setText(json.getString("name"));
                                    postSummary.setText(json.getString("summary"));
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                                layout.addView(relLayout, i);
                                set.clone(layout);
                                if (previousLayout != null)
                                    set.connect(relLayout.getId(), ConstraintSet.TOP, previousLayout.getId(), ConstraintSet.BOTTOM, 60);
                                set.applyTo(layout);
                                previousLayout = relLayout;
                            }
                        }
                    });
                }
            }
        });
    }

    public void to_post(View view){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void to_main(View view){
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }

    public void to_notifications(View view){
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
}
