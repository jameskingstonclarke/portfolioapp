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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DiscoveryFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_feed);
        createProjectPostUI();
        createUserPostUI();
    }

    public void createProjectPostUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "all");
        Requests.postRequestToken(Urls.PROJECT_POST, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(String reply) {
                        JSONArray tmpArray = null;
                        try{
                            tmpArray = new JSONArray(reply);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        final JSONArray jsonArray = tmpArray;
                        // Attempt to retrieve the post count
                        final int postCount = jsonArray.length();
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
                                    TextView skills = relLayout.findViewById(R.id.project_post_skills);
                                    try {
                                        JSONObject json = (JSONObject)jsonArray.get(i);
                                        postTitle.setText(json.getString("title"));
                                        description.setText(json.getString("description"));
                                        user.setText("by "+json.getString("user_name"));
                                        time.setText(json.getString("time"));
                                        skills.setText("skills: ");
                                        JSONArray skillsArray = json.getJSONArray("skills");
                                        for(int skillIndex = 0; skillIndex<skillsArray.length(); skillIndex++) {
                                            skills.setText(
                                                    skillIndex == 0 ? skills.getText() + (String) skillsArray.get(skillIndex) : skills.getText() + ", " + skillsArray.get(skillIndex)
                                            );
                                        }
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

    public void createUserPostUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "all");
        Requests.postRequestToken(Urls.USER_POST, data,
                new Requests.RequestOperation() {
                    @Override
                    public JSONObject onReply(String reply) {
                        JSONArray tmpArray = null;
                        try{
                            tmpArray = new JSONArray(reply);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        final JSONArray jsonArray = tmpArray;
                        // Attempt to retrieve the post count
                        final int postCount = jsonArray.length();
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
