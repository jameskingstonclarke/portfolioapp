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
import java.util.HashMap;

public class MainFeedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        createProjectPostUI();
    }

    public void createProjectPostUI(){
        HashMap<String, String> data = new HashMap<>();
        data.put("function", "all");
        Requests.postRequestToken(false, Urls.PROJECT_POST, data,
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
                        MainFeedActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Dynamically invoke stuff here
                                ConstraintSet set = new ConstraintSet();
                                ConstraintLayout layout = findViewById(R.id.mainProjectPostScrollConstraint);
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

    public void to_post(View view){
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void to_discover(View view){
        Intent intent = new Intent(this, DiscoveryFeedActivity.class);
        startActivity(intent);
    }

    public void to_notifications(View view){
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }
}
